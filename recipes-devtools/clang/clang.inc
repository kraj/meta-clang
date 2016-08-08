LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "release_39"
SRCREV_llvm = "2c01070bc51ac2ac8f9cff5e564f3ae09126f47e"
SRCREV_clang = "29427626008fc4322bb820814b3257981581e63d"
SRCREV_compiler-rt = "85cd828f87c0800e698b8d62334b2e1270375307"
SRCREV_cxxabi = "82bb4c89734b6157de4a2fc2b9228be6529337a8"
SRCREV_libcxx = "95fa236b0059899e96b4092e66e776608c4e3c52"
SRCREV_libunwind = "4194a6d954d2d6bfb225d22411cc3bc9932cc76f"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
