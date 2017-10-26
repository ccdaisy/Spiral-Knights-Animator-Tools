# The big 'ol to-do list.

#### Key:
Plain text = Not done

*Italics* = Working on it

~~Strikethrough~~ = Done


**General:**
* ~~Add better handlers for unknown data types.~~

**Implementations (Model types):**
* ~~Support for ArticulatedConfig~~ (Animated rigs, characters usually)
   * ~~Support for Attachments~~
* ~~Support for CompoundConfig~~ (Same as a group - Contains multiple models in one major piece)
   * ~~Support for loading references without having to create new objects internally.~~
* ~~Support for GeneratedStaticConfig (Models created within code manually.)~~
   * **Note: This format cannot be converted, but it is not used as a file. GeneratedStatic models are generated within code.**
* ~~Support for MergedStaticConfig~~ (Similar to CompoundConfig, but is instead intended to function as one large static piece even though it is comprised of multiple pieces)
* ~~Support for StaticConfig~~ (Static, unmoving models)
* ~~Support for StaticSetConfig~~ (Multiple static models that are normally separate (unlike MergedStaticConfig))
* ~~Support for other configs~~
* **~~Support for ProjectXModelConfig (Or, better known as "Knights")~~** (NOTE: This update will not be appended. Through further research I found that ProjectXModelConfig knight models are intended to be controlled by players. This config allows for the things like seperate leg animations based on where you're facing vs where you're moving. There is another knight model, luckily.)

**Export:**
* Finish upgraded DAE exporter.
   * ~~Finish base model exporting~~
   * *Finish bone data output*
   * Animation output
