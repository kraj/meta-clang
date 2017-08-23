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
SRCREV_llvm = "70a89bb1406c19144e2ff7c6f36d0c0e46498d26"
SRCREV_clang = "8e4862b5be221092291aaf7ea40d72274acb9d96"
SRCREV_compiler-rt = "e6bb43d8b68ab16a71b060fc32fcba18d20f8828"
SRCREV_cxxabi = "5df6b5da0deba63bbf9046bcaa385241c4d72847"
SRCREV_libcxx = "3fa4f9bbd1408f9b42b5a92c937cc832f3061ee8"
SRCREV_libunwind = "08a9d8430b8096d8963e9bc9c2a05f32816dd8a0"
SRCREV_lld = "aee49a42ef600fa272f8e4a845e5fc847bcd427e"
SRCREV_lldb = "c15ef4dfe4ef4470ad5c53a9b1bf2ee15807e376"
SRCREV_openmp = "1333cd34fa88de8461c0dbd2fbd439321073a334"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDMD5SUM = "c7343e1ccb3d65a14eba72ce1d548cb4"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
