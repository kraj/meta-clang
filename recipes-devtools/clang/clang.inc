LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "f79d978b96168a0155772936fd75eab7527c4124"
SRCREV_clang = "a9119fb208274ebacec020c73768827e74ec5214"
SRCREV_compiler-rt = "3df39427e495cb2ae84b9d7fc8541d4601f7b597"
SRCREV_libcxxabi = "5328c6b635a145efc1204ad25341706fb705e346"
SRCREV_libcxx = "8e83446812dec68dac6baf7530d17b82549be40f"
SRCREV_libunwind = "7a00ec9517645046685fd6c115f0b1dc775f0bb3"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
