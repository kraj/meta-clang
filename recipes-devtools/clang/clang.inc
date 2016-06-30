LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "18b9b9a1f4d2582b2b2ff240f39f7298f3fe5708"
SRCREV_clang = "9f075b782a61c9af1b8652d2a4bd7d0165789830"
SRCREV_compiler-rt = "5981c629cb4879ed91a74c6f88ad96e8996f2ee5"
SRCREV_libcxxabi = "5328c6b635a145efc1204ad25341706fb705e346"
SRCREV_libcxx = "70bf1c2280d514792b4bc6035c4685e82da79645"
SRCREV_libunwind = "7a00ec9517645046685fd6c115f0b1dc775f0bb3"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
