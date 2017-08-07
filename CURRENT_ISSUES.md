# Resolved Issues

**001: Any ArticulatedConfig type models with no bone data (no id for boneIndices / boneWeights) will still treat the model as if it has specific datapoints for these values, causing it to read incorrectly.**
 - Resolved on: August 7, 2017

# Live issues
 
**002: Errors throwing on any implementations where attempting to find data that doesn't exist, i.e. scripts or animations**
 - Issue severity: Major (Complete program failure)
 - Issue details: Happens mainly on compound models that reference a set of other models. Converter will throw an EOFException when it cannot find the first set of data.
 - Estimated fix time: 2-3 hours or more due to the requirement of live references to other files.

**003: No reliable way to locate stride and offset values for datatypes**
 - Issue severity: Intermediate (Read flaws)
 - Issue details: The current application uses an assumed set of values for stride and offset due to the general frequency of those values being extremely high in models. It does not actually read the config to find the value due to the lack of a reliable method of finding where that data is in the binary (hence this issue)
  - Extimated fix time: 1-2 hours
