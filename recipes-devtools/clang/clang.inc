LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "release_39"
SRCREV_llvm = "63485bd162ef11dcfbd313e10f2e45d6f2c0d58e"
SRCREV_clang = "1ce520b4b2d9ff3f233e30c917d9e888abb49daf"
SRCREV_compiler-rt = "95277fbc5a050588234bc4e2a52d7902e663e3b1"
SRCREV_cxxabi = "82bb4c89734b6157de4a2fc2b9228be6529337a8"
SRCREV_libcxx = "95fa236b0059899e96b4092e66e776608c4e3c52"
SRCREV_libunwind = "4194a6d954d2d6bfb225d22411cc3bc9932cc76f"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
