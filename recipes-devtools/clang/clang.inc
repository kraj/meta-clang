LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#
MAJOR_VER="6"
MINOR_VER="0"
PATCH_VER="0"
PV = "${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}"
BRANCH = "release_60"
SRCREV_llvm = "72d068a93fe1cd266b091afb9d257b27a18e9be1"
SRCREV_clang = "bb8d45f8ab88237f1fa0530b8ad9b96bf4a5e6cc"
SRCREV_compiler-rt = "9d61c78bced84866cc886f1f1111c8e51c1d52d5"
SRCREV_cxxabi = "9245d481eb3e890f708ff2d7dadf2a10c04748ba"
SRCREV_libcxx = "48a89ecb9fd90f403ad8a7b061ff1bb18f34e132"
SRCREV_libunwind = "68c1bfc35687033aad28657404b80b1d558a4804"
SRCREV_lld = "20c4fe685fc4ebb2ee52331e4c2a66ff35cb82dd"
SRCREV_lldb = "50b0daa322b7e77c6064e0327cb0824d8f2bbba1"
SRCREV_openmp = "d5aa29cb3bcf51289d326b4e565613db8aff65ef"

LLVMMD5SUM = "e825e017edc35cfd58e26116e5251771"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
LLDMD5SUM = "c7343e1ccb3d65a14eba72ce1d548cb4"
LLDBMD5SUM = "b6320ed0b0d00ae661dd94f277bbf024"
