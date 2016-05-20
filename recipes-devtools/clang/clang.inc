LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "${AUTOREV}"
SRCREV_clang = "${AUTOREV}"
SRCREV_compiler-rt = "${AUTOREV}"
SRCREV_libcxxabi = "${AUTOREV}"
SRCREV_libcxx = "${AUTOREV}"
SRCREV_libunwind = "${AUTOREV}"
LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
