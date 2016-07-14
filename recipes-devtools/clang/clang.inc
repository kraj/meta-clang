LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "84f3bec4220ce8a415672f375b4155927e400ebf"
SRCREV_clang = "d5dff991007e9305f825cc274d768953ff13eda5"
SRCREV_compiler-rt = "e42331093b119be509dc346b57544d1777d80de4"
SRCREV_cxxabi = "5328c6b635a145efc1204ad25341706fb705e346"
SRCREV_libcxx = "a80de495f78131863bcbcb722e703e0623bfeb99"
SRCREV_libunwind = "878c40b2f240ee9f57be7dce86caf7650f79dc7f"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
