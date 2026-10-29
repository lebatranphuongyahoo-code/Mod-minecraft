import { world, system, ItemStack } from "@minecraft/server";

const TYPES = ["earthquake", "tornado", "volcano", "meteor", "tsunami", "wildfire", "blizzard", "thunderstorm", "sandstorm"];
const DISPLAY = { earthquake: "Earthquake", tornado: "Tornado", volcano: "Volcanic Eruption", meteor: "Meteor Shower", tsunami: "Tsunami / Flood", wildfire: "Wildfire", blizzard: "Blizzard", thunderstorm: "Lightning Storm", sandstorm: "Sandstorm" };
const state = { active: null, warning: 0, cooldown: 0, automatic: true, elapsed: 0, frequency: 36000, safeMode: false, maxIntensity: 5 };

function tell(player, text) { player.sendMessage(`§6[Cataclysm] §f${text}`); }
function tellAll(text) { for (const p of world.getAllPlayers()) tell(p, text); }
function pos(player) { return { x: Math.floor(player.location.x), y: Math.floor(player.location.y), z: Math.floor(player.location.z) }; }
function run(dimension, command) { try { dimension.runCommand(command); } catch (_) {} }
function around(player, radius) { return { x: Math.floor(player.location.x + (Math.random() * 2 - 1) * radius), y: Math.floor(player.location.y), z: Math.floor(player.location.z + (Math.random() * 2 - 1) * radius) }; }
function particle(player, name, p) { try { player.dimension.spawnParticle(name, p); } catch (_) {} }

function start(type, intensity = 1, seconds = 120) {
  if (!TYPES.includes(type)) return false;
  if (state.active) return false;
  intensity = Math.max(1, Math.min(state.maxIntensity, Number(intensity) || 1));
  state.active = { type, intensity, ticks: Math.max(20, seconds * 20), total: Math.max(20, seconds * 20) };
  state.warning = 20 * 15;
  tellAll(`§cWARNING: ${DISPLAY[type]}§f incoming. Intensity ${intensity}. Prepare shelter!`);
  for (const p of world.getAllPlayers()) run(p.dimension, `playsound random.explode @a ~ ~ ~ 0.5 0.5`);
  return true;
}
function stop() { if (state.active) tellAll(`§a${DISPLAY[state.active.type]} has ended.`); state.active = null; state.warning = 0; state.cooldown = 1200; }

function damagePlayers(amount, cause = "entityAttack") {
  for (const p of world.getAllPlayers()) { try { p.applyDamage(amount, { cause }); } catch (_) { try { p.applyDamage(amount); } catch (_) {} } }
}
function disasterTick() {
  const d = state.active; if (!d) return;
  const players = world.getAllPlayers(); if (!players.length) return;
  const lead = players[0]; const radius = 8 + d.intensity * 5;
  if (state.warning > 0) { state.warning--; if (state.warning % 100 === 0) tellAll(`§e${DISPLAY[d.type]} in ${Math.ceil(state.warning / 20)} seconds`); return; }
  d.ticks--;
  if (d.type === "earthquake") { if (d.ticks % 10 === 0) damagePlayers(d.intensity * 0.4); if (d.ticks % 6 === 0 && !state.safeMode) run(lead.dimension, `fill ~-2 ~ ~-2 ~2 ~ ~2 minecraft:cracked_stone_bricks replace minecraft:stone`); }
  if (d.type === "tornado") { if (d.ticks % 5 === 0) { for (const p of players) { p.addEffect("levitation", 8, { amplifier: Math.min(2, d.intensity - 1), showParticles: false }); particle(p, "minecraft:basic_smoke_particle", around(p, radius)); } } if (d.ticks % 20 === 0) damagePlayers(d.intensity * 0.25); }
  if (d.type === "volcano") { if (d.ticks % 12 === 0) { const p = around(lead, radius); run(lead.dimension, `setblock ${p.x} ${p.y} ${p.z} minecraft:lava`); particle(lead, "minecraft:lava_particle", { x: p.x + .5, y: p.y + 1, z: p.z + .5 }); } if (d.ticks % 30 === 0) damagePlayers(d.intensity * .5); }
  if (d.type === "meteor") { if (d.ticks % Math.max(15, 70 - d.intensity * 8) === 0) { const p = around(lead, radius * 2); run(lead.dimension, `summon lightning_bolt ${p.x} ${p.y} ${p.z}`); run(lead.dimension, `fill ${p.x - 2} ${p.y} ${p.z - 2} ${p.x + 2} ${p.y} ${p.z + 2} minecraft:fire replace air`); run(lead.dimension, `playsound random.explode @a ${p.x} ${p.y} ${p.z} 2 0.7`); } }
  if (d.type === "tsunami") { if (d.ticks % 15 === 0 && !state.safeMode) { const p = around(lead, radius); run(lead.dimension, `fill ${p.x - 2} ${p.y} ${p.z - 2} ${p.x + 2} ${p.y + 1} ${p.z + 2} minecraft:water replace air`); } }
  if (d.type === "wildfire") { if (d.ticks % 12 === 0 && !state.safeMode) { const p = around(lead, radius); run(lead.dimension, `setblock ${p.x} ${p.y} ${p.z} minecraft:fire`); particle(lead, "minecraft:basic_flame_particle", { x: p.x + .5, y: p.y + 1, z: p.z + .5 }); } if (d.ticks % 20 === 0) damagePlayers(d.intensity * .3, "fire"); }
  if (d.type === "blizzard") { for (const p of players) { p.addEffect("slowness", 30, { amplifier: d.intensity - 1, showParticles: false }); particle(p, "minecraft:snowflake_particle", around(p, radius)); } if (d.ticks % 40 === 0) damagePlayers(d.intensity * .2, "freezing"); }
  if (d.type === "thunderstorm") { if (d.ticks % Math.max(10, 45 - d.intensity * 5) === 0) { const p = around(lead, radius); run(lead.dimension, `summon lightning_bolt ${p.x} ${p.y} ${p.z}`); } }
  if (d.type === "sandstorm") { for (const p of players) { p.addEffect("blindness", 25, { amplifier: 0, showParticles: false }); particle(p, "minecraft:falling_dust", around(p, radius)); } if (d.ticks % 30 === 0) damagePlayers(d.intensity * .25); }
  if (d.ticks <= 0) stop();
}

function command(player, text) {
  const a = text.trim().split(/\s+/); if (a[0] !== "!disaster") return;
  if (a[1] === "list") return tell(player, TYPES.join(", "));
  if (a[1] === "stop") return stop();
  if (a[1] === "schedule") { state.automatic = a[2] === "on"; return tell(player, `Automatic schedule: ${state.automatic ? "on" : "off"}`); }
  if (a[1] === "safe") { state.safeMode = a[2] !== "off"; return tell(player, `Safe mode: ${state.safeMode ? "on" : "off"}`); }
  if (a[1] === "start") return tell(player, start(a[2], a[3], a[4]) ? "Disaster started." : "Cannot start: unknown type or another disaster is active.");
  tell(player, "Usage: !disaster start <type> [intensity] [seconds], !disaster stop, !disaster list, !disaster schedule <on|off>");
}

world.beforeEvents.chatSend.subscribe(ev => { if (ev.message.startsWith("!disaster")) { ev.cancel = true; system.run(() => command(ev.sender, ev.message)); } });
system.runInterval(() => { if (state.cooldown > 0) state.cooldown--; if (!state.active && state.automatic) { state.elapsed++; if (state.elapsed >= state.frequency) { state.elapsed = 0; start(TYPES[Math.floor(Math.random() * TYPES.length)], 1 + Math.floor(Math.random() * state.maxIntensity), 120); } } disasterTick(); }, 1);
world.afterEvents.playerSpawn.subscribe(ev => { if (ev.initialSpawn) tell(ev.player, "Natural Disasters active. Use !disaster list for available events."); });
