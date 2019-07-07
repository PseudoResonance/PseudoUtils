package io.github.pseudoresonance.pseudoutils.commands;

import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import io.github.pseudoresonance.pseudoapi.bukkit.Message.Errors;
import io.github.pseudoresonance.pseudoapi.bukkit.SubCommandExecutor;
import io.github.pseudoresonance.pseudoapi.bukkit.utils.CommandUtils;
import io.github.pseudoresonance.pseudoutils.PseudoUtils;

public class TpSC implements SubCommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.tp"))) {
			Player p = null;
			if (args.length == 0) {
				PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a target and location!");
			} else if (args.length == 1) {
				if (sender instanceof Player) {
					p = (Player) sender;
					try {
						Entity entity = CommandUtils.getTarget(sender, args[0]);
						if (entity != null) {
							p.teleport(entity);
							PseudoUtils.message.sendPluginMessage(sender, "Teleported to " + entity.getName());
							return true;
						} else {
							PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "No target found!");
							return false;
						}
					} catch (Exception e) {
						PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Entity selectors do not work in this version!");
						return false;
					}
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a target and location!");
					return false;
				}
			} else if (args.length == 2) {
				if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.tp.others"))) {
					try {
						Entity[] entities = CommandUtils.getTargets(sender, args[0]);
						Entity entity = CommandUtils.getTarget(sender, args[1]);
						if (entities.length == 0) {
							PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "No entities found!");
							return false;
						}
						if (entity != null) {
							for (Entity e : entities) {
								e.teleport(entity);
							}
							if (entities.length == 1) {
								PseudoUtils.message.sendPluginMessage(sender, "Teleported " + entities[0].getName() + " to " + entity.getName());
							} else {
								PseudoUtils.message.sendPluginMessage(sender, "Teleported " + entities.length + " entities to " + entity.getName());
							}
							return true;
						} else {
							PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "No target found!");
							return false;
						}
					} catch (Exception e) {
						PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Entity selectors do not work in this version!");
						return false;
					}
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "teleport other players!");
				}
			} else if (args.length == 3) {
				if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.tp.coordinates"))) {
					if (sender instanceof Player) {
						p = (Player) sender;
						try {
							double[] coords = CommandUtils.getRelativeCoords(args[0], args[1], args[2], p);
							p.teleport(new Location(p.getWorld(), coords[0], coords[1], coords[2], p.getLocation().getYaw(), p.getLocation().getPitch()));
							PseudoUtils.message.sendPluginMessage(sender, "Teleported to " + coords[0] + ", " + coords[1] + ", " + coords[2]);
						} catch (NumberFormatException e) {
							PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid coordinates!");
						}
					} else {
						PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a target!");
						return false;
					}
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "teleport to coordinates!");
				}
			} else if (args.length == 4) {
				if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.tp.coordinates"))) {
					try {
						if (args[3].length() == 36) {
							if (sender instanceof Player) {
								p = (Player) sender;
								double[] coords = CommandUtils.getRelativeCoords(args[0], args[1], args[2], p);
								try {
									World w = PseudoUtils.plugin.getServer().getWorld(UUID.fromString(args[3]));
									if (w == null) {
										PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid world UUID!");
										return false;
									} else {
										p.teleport(new Location(w, coords[0], coords[1], coords[2], p.getLocation().getYaw(), p.getLocation().getPitch()));
										PseudoUtils.message.sendPluginMessage(sender, "Teleported to " + coords[0] + ", " + coords[1] + ", " + coords[2] + " in world: " + w.getName());
										return true;
									}
								} catch (IllegalArgumentException e) {
									PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid world UUID!");
									return false;
								}
							} else {
								PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a target!");
								return false;
							}
						} else {
							if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.tp.others"))) {
								try {
									Entity[] entities = CommandUtils.getTargets(sender, args[0]);
									if (entities.length == 0) {
										PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "No entities found!");
										return false;
									}
									double[] coords = CommandUtils.getRelativeCoords(args[1], args[2], args[3], sender);
									for (Entity e : entities) {
										e.teleport(new Location(e.getWorld(), coords[0], coords[1], coords[2], e.getLocation().getYaw(), e.getLocation().getPitch()));
									}
									if (entities.length == 1) {
										PseudoUtils.message.sendPluginMessage(sender, "Teleported " + entities[0].getName() + " to " + coords[0] + ", " + coords[1] + ", " + coords[2]);
									} else {
										PseudoUtils.message.sendPluginMessage(sender, "Teleported " + entities.length + " entities to " + coords[0] + ", " + coords[1] + ", " + coords[2]);
									}
									return true;
								} catch (Exception e) {
									PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Entity selectors do not work in this version!");
									return false;
								}
							} else {
								PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "teleport other players!");
							}
						}
					} catch (NumberFormatException e) {
						PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid coordinates!");
					}
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "teleport to coordinates!");
				}
			} else if (args.length == 5) {
				if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.tp.coordinates"))) {
					try {
						if (args[4].length() == 36) {
							if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.tp.others"))) {
								try {
									Entity[] entities = CommandUtils.getTargets(sender, args[0]);
									if (entities.length == 0) {
										PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "No entities found!");
										return false;
									}
									double[] coords = CommandUtils.getRelativeCoords(args[1], args[2], args[3], sender);
									try {
										World w = PseudoUtils.plugin.getServer().getWorld(UUID.fromString(args[4]));
										if (w == null) {
											PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid world UUID!");
											return false;
										} else {
											for (Entity e : entities) {
												e.teleport(new Location(w, coords[0], coords[1], coords[2], e.getLocation().getYaw(), e.getLocation().getPitch()));
											}
											if (entities.length == 1) {
												PseudoUtils.message.sendPluginMessage(sender, "Teleported " + entities[0].getName() + " to " + coords[0] + ", " + coords[1] + ", " + coords[2] + " in world: " + w.getName());
											} else {
												PseudoUtils.message.sendPluginMessage(sender, "Teleported " + entities.length + " entities to " + coords[0] + ", " + coords[1] + ", " + coords[2] + " in world: " + w.getName());
											}
											return true;
										}
									} catch (IllegalArgumentException e) {
										PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid world UUID!");
										return false;
									}
								} catch (Exception e) {
									PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Entity selectors do not work in this version!");
									return false;
								}
							} else {
								PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "teleport other players!");
								return false;
							}
						} else {
							if (sender instanceof Player) {
								p = (Player) sender;
								float yaw = Float.valueOf(args[3]);
								float pitch = Float.valueOf(args[4]);
								double[] coords = CommandUtils.getRelativeCoords(args[0], args[1], args[2], p);
								p.teleport(new Location(p.getWorld(), coords[0], coords[1], coords[2], yaw, pitch));
								PseudoUtils.message.sendPluginMessage(sender, "Teleported to " + coords[0] + ", " + coords[1] + ", " + coords[2] + " yaw: " + yaw + " pitch: " + pitch);
							} else {
								PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a target!");
								return false;
							}
						}
					} catch (NumberFormatException e) {
						PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid coordinates!");
						return false;
					}
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "teleport to coordinates!");
					return false;
				}
			} else if (args.length == 6) {
				if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.tp.coordinates"))) {
					try {
						if (args[5].length() == 36) {
							if (sender instanceof Player) {
								p = (Player) sender;
								float yaw = Float.valueOf(args[3]);
								float pitch = Float.valueOf(args[4]);
								double[] coords = CommandUtils.getRelativeCoords(args[0], args[1], args[2], p);
								try {
									World w = PseudoUtils.plugin.getServer().getWorld(UUID.fromString(args[5]));
									if (w == null) {
										PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid world UUID!");
										return false;
									} else {
										p.teleport(new Location(w, coords[0], coords[1], coords[2], yaw, pitch));
										PseudoUtils.message.sendPluginMessage(sender, "Teleported to " + coords[0] + ", " + coords[1] + ", " + coords[2] + " yaw: " + yaw + " pitch: " + pitch + " in world: " + w.getName());
										return true;
									}
								} catch (IllegalArgumentException e) {
									PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid world UUID!");
									return false;
								}
							} else {
								PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Please specify a target!");
								return false;
							}
						} else {
							if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.tp.others"))) {
								try {
									Entity[] entities = CommandUtils.getTargets(sender, args[0]);
									if (entities.length == 0) {
										PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "No entities found!");
										return false;
									}
									float yaw = Float.valueOf(args[4]);
									float pitch = Float.valueOf(args[5]);
									double[] coords = CommandUtils.getRelativeCoords(args[1], args[2], args[3], sender);
									for (Entity e : entities) {
										e.teleport(new Location(e.getWorld(), coords[0], coords[1], coords[2], yaw, pitch));
									}
									if (entities.length == 1) {
										PseudoUtils.message.sendPluginMessage(sender, "Teleported " + entities[0].getName() + " to " + coords[0] + ", " + coords[1] + ", " + coords[2] + " yaw: " + yaw + " pitch: " + pitch);
									} else {
										PseudoUtils.message.sendPluginMessage(sender, "Teleported " + entities.length + " entities to " + coords[0] + ", " + coords[1] + ", " + coords[2] + " yaw: " + yaw + " pitch: " + pitch);
									}
									return true;
								} catch (Exception e) {
									PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Entity selectors do not work in this version!");
									return false;
								}
							} else {
								PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "teleport other players!");
							}
						}
					} catch (NumberFormatException e) {
						PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid coordinates!");
					}
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "teleport to coordinates!");
				}
			} else if (args.length == 7) {
				if (!(sender instanceof Player) || (sender.hasPermission("pseudoutils.tp.coordinates") && sender.hasPermission("pseudoutils.tp.other"))) {
					try {
						Entity[] entities = CommandUtils.getTargets(sender, args[0]);
						if (entities.length == 0) {
							PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "No entities found!");
							return false;
						}
						float yaw = Float.valueOf(args[4]);
						float pitch = Float.valueOf(args[5]);
						double[] coords = CommandUtils.getRelativeCoords(args[1], args[2], args[3], sender);
						try {
							World w = PseudoUtils.plugin.getServer().getWorld(UUID.fromString(args[6]));
							if (w == null) {
								PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid world UUID!");
								return false;
							} else {
								for (Entity e : entities) {
									e.teleport(new Location(w, coords[0], coords[1], coords[2], yaw, pitch));
								}
								if (entities.length == 1) {
									PseudoUtils.message.sendPluginMessage(sender, "Teleported " + entities[0].getName() + " to " + coords[0] + ", " + coords[1] + ", " + coords[2] + " yaw: " + yaw + " pitch: " + pitch + " in world: " + w.getName());
								} else {
									PseudoUtils.message.sendPluginMessage(sender, "Teleported " + entities.length + " entities to " + coords[0] + ", " + coords[1] + ", " + coords[2] + " yaw: " + yaw + " pitch: " + pitch + " in world: " + w.getName());
								}
								return true;
							}
						} catch (IllegalArgumentException e) {
							PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid world UUID!");
							return false;
						}
					} catch (Exception e) {
						PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Entity selectors do not work in this version!");
						return false;
					}
				} else {
					PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "teleport other players to coordinates!");
				}
			} else {
				PseudoUtils.message.sendPluginError(sender, Errors.CUSTOM, "Invalid arguments!");
			}
		} else
			PseudoUtils.message.sendPluginError(sender, Errors.NO_PERMISSION, "teleport!");
		return false;
	}

}
