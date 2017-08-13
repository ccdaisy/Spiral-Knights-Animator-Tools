# About the project
This is a revamp of the old, crappy tool dubbed [Spiral Knights Model Converter](https://github.com/XanTheDragon/Spiral-Knights-Model-Converter).

# When can I try it?
There may be a release up in the releases tab. At times I'll remove them.

# When is it truly out and ready for full usage?
~~My rough guess is August 13 at a maximum (Two weeks from now, July 30). I'm actively working.~~

Sorry guys, Deadline Machine 🅱roke, and school starts tomorrow for me. (Upd. August 13) Right now I'm finishing up bone recognition as well as condensing the Vector3 + Quaternion into a 4D matrix (Because DAEs are really picky about that)

(**Update: There might be a slight delay given that I may not be able to implement full reading immediately. There are still some intermittent issues with certain models and working out the issues has been a slow process.**)

(Deadline update, posted August 6 @ 3:09 AM - I'm *thinking* that this might be an accurate release date. I've almost got it down for solid model exporting. At the moment I've only got obj exporting as a crude prototype. It has issues with certain implementations but is otherwise exporting perfectly)

# What makes this version better?

* 100% handwritten code - No more using other libraries.
 * Considerably smaller jar size
* Direct binary file reading
 * Considerably faster conversion
 * Guaranteed support for any **model** file (Things like scripts will not convert since they aren't 3D models.)
* **Commnand line support**
* The GUI still works, and has been redone to be much smaller and straightforward.

# Where can I view development progress? 

You can refer to [CURRENT_ISSUES.MD](https://github.com/XanTheDragon/Spiral-Knights-Animator-Tools/blob/master/CURRENT_ISSUES.md) for the list of issues that are currently hindering development right now.
