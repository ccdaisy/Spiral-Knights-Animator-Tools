### This is the longlist of issues currently found in the private development build of the converter.

**Issue 001: Any ArticulatedConfig type models with no bone data (no id for boneIndices / boneWeights) will still treat the model as if it has specific datapoints for these values, causing it to read incorrectly.**
 - Issue severity: Intermediate
 - Estimated fix time: 30-120 minutes, depending on what's needed to create a workaround.
 
**Issue 002: Errors throwing on any implementations where attempting to find data that doesn't exist, i.e. scripts or animations**
 - Issue severity: Major
 - Issue details: Happens mainly on compound models that reference a set of other models. Converter will throw an EOFException when it cannot find the first set of data.
 - Estimated fix time: 2-3 hours or more due to the requirement of live references to other files.
