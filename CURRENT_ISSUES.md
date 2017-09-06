# Resolved Issues

**001: Any ArticulatedConfig type models with no bone data (no id for boneIndices / boneWeights) will still treat the model as if it has specific datapoints for these values, causing it to read incorrectly.**
 - Resolved on: August 7, 2017
 
**003: No reliable way to locate stride and offset values for datatypes**
 - Resolved on August 8, 2017

**Fix note:** ~~The static values seem to actually be universal, as if they are a base value. It seems that when a value is removed, each stride/offset value loses a static amount (I'm thinking it's 4). In the case of one of the models with no bone data, that would be -8 (no bone indices, no bone weights). However, 4 happens to be the size of those data types. Perhaps I subtract the size of the value that isn't present from the others' strides and offsets.~~
 
**Fix note:** https://xanthedragon.blogspot.com/2017/08/progress-report-6-amazing-discovery.html
 
# Issues being fixed right now

# Live issues
 
**002: Errors throwing on any implementations where attempting to find data that doesn't exist, i.e. scripts or animations**
 - Issue severity: Major (Complete program failure)
 - Issue details: Happens mainly on compound models that reference a set of other models. Converter will throw an EOFException when it cannot find the first set of data that is specifically queried in any geometry model.
 - Estimated fix time: 2-3 hours or more due to the requirement of live references to other files.

**004: Knight model implementations are tagged as "malformed" in the converter**
 - Issue severity: Intermediate (Conversion failure, handled by code)
 - Issue details: Knights use a custom implementation. The code used to open models is set up to read specific implementations (Articulated, Compound, Static, etc.) but since that code is open source, it's designed for general usage. Spiral Knights does use those libraries, but since Spiral Knights is a standalone game it has its own implementation designed specifically for knights. This is why Spiral Spy must be placed in the SK Directory. The Knight implementation is called "projectXConfig"
 - Estimated fix time: Unknown.
