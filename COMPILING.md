# Compiling

This project depends on NMS, which shouldn't be redistributed. This means you'll have to build some of the dependencies yourself. (TODO: a script/tool for this would be handy)

How to compile:

1. Use BuildTools normally and create a remapped jar (use `--remapped`) for every NMS version UC depends on. As of this writing, those versions are 1.19.4 and 1.20.2.
2. Open a shell in the UC folder, and run `./gradlew obfuscate`. Using tasks `build` or `jar` will not produce a jar that works with NMS because of the Mojang mappings.
3. The built UC jar is at `build/libs/UltraCosmetics-<version>-<buildtype>.jar`. Do NOT use the other jar that says `obfuscated-donotuse`, it won't work.

And you're done!
