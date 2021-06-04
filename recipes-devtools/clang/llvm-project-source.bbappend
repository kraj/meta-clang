# This needs to override meta-intel (which pulls in the
# LLVM 10/11 version of SPIRV-LLVM-Translator)

SPIRV_BRANCH = "llvm_release_120"
SRC_URI_append_intel-x86-common = " \
                                    git://github.com/KhronosGroup/SPIRV-LLVM-Translator.git;protocol=https;branch=${SPIRV_BRANCH};destsuffix=git/llvm/projects/llvm-spirv;name=spirv \
                                    "
SRCREV_spirv = "67d3e271a28287b2c92ecef2f5e98c49134e5946"

