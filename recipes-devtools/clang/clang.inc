LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "2a09a5fe752117113500a8353c80ad5a7105e6f2"
SRCREV_clang = "700e6a514a37a0eb2f26c27baf33163ad07f5b15"
SRCREV_compiler-rt = "3e3f7da05ecff5798c252a3bba17138ffb636fcf"
SRCREV_cxxabi = "5328c6b635a145efc1204ad25341706fb705e346"
SRCREV_libcxx = "576639133af7bd202a285117818a5575d356e3be"
SRCREV_libunwind = "878c40b2f240ee9f57be7dce86caf7650f79dc7f"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
