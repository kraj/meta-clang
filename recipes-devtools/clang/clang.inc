LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "3684019e63a27796455bc92e267ed15fb8432e7c"
SRCREV_clang = "6f24f599e229f7f767abd7226a9bb21da4bbed31"
SRCREV_compiler-rt = "e74f92f308c93cd44e9c53e3e0b0e20766802bda"
SRCREV_libcxxabi = "5985dd69c9ef90f3eb13658253bd7800c16db4e1"
SRCREV_libcxx = "fc1962da3749624eb1b66291c99258fcbad4ca98"
SRCREV_libunwind = "7b1a88cf2a276f26d7f22c09f05accd3c750ca35"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
