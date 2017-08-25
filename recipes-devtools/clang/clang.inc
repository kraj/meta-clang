LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#
MAJOR_VER="5"
MINOR_VER="0"
PATCH_VER="0"
PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release_50"
SRCREV_llvm = "3359933e710d5dae2815cf2fd3d776dfb3ffe1fa"
SRCREV_clang = "0bc78694a319f80a28ca30e4d9d69c292ee12dee"
SRCREV_compiler-rt = "4b38c4038a4f2b8e2d02b5f5d7877fa79d940009"
SRCREV_cxxabi = "5df6b5da0deba63bbf9046bcaa385241c4d72847"
SRCREV_libcxx = "3fa4f9bbd1408f9b42b5a92c937cc832f3061ee8"
SRCREV_libunwind = "08a9d8430b8096d8963e9bc9c2a05f32816dd8a0"
SRCREV_lld = "e2974bce18137935bde80dd34acb255cf6c68db0"
SRCREV_lldb = "d39b064b9bed74a50147436405d28b96502adc32"
SRCREV_openmp = "1333cd34fa88de8461c0dbd2fbd439321073a334"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDMD5SUM = "c7343e1ccb3d65a14eba72ce1d548cb4"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
