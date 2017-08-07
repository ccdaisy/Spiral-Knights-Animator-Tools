# Resolved Issues

**001: Any ArticulatedConfig type models with no bone data (no id for boneIndices / boneWeights) will still treat the model as if it has specific datapoints for these values, causing it to read incorrectly.**
 - Resolved on: August 7, 2017
 
**003: No reliable way to locate stride and offset values for datatypes**
 - Resolved on August 8, 2017

**Fix note:** ~~The static values seem to actually be universal, as if they are a base value. It seems that when a value is removed, each stride/offset value loses a static amount (I'm thinking it's 4). In the case of one of the models with no bone data, that would be -8 (no bone indices, no bone weights). However, 4 happens to be the size of those data types. Perhaps I subtract the size of the value that isn't present from the others' strides and offsets.~~
 
**Fix note:** Actually, offset seems to be calculated!
The equasion is: `lastOffset + (thisSize * 4) = nextOffset`, and it's consistent.
```
boneIndices(Offset=0, Stride=64, Size=4)
boneWeights(Offset=16, Stride=64, Size=4)
texCoords(Offset=32, Stride=64, Size=2)
normals(Offset=40, Stride=64, Size=3)
vertices(Offset=52, Stride=64, Size=3)

boneIndices = 0
0 + (4 * 4) = 16 (boneWeights)
16 + (4 * 4) = 32 (texCoords)
32 + (2 * 4) = 40 (normals)
40 + (3 * 4) = 52 (vertices)
```
 
# Issues being fixed right now

# Live issues
 
**002: Errors throwing on any implementations where attempting to find data that doesn't exist, i.e. scripts or animations**
 - Issue severity: Major (Complete program failure)
 - Issue details: Happens mainly on compound models that reference a set of other models. Converter will throw an EOFException when it cannot find the first set of data.
 - Estimated fix time: 2-3 hours or more due to the requirement of live references to other files.
