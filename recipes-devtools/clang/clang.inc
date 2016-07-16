LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "2e9433d42da2a7bc756b7c1cb4a654dc906a7e7e"
SRCREV_clang = "a29693086ca0a0182832157dfb234a4a6135370e"
SRCREV_compiler-rt = "6b635bc08dd7c8313dba85f6d1877cbceef38f76"
SRCREV_cxxabi = "f0a26203e7ce856ccf7c12c2c1c9d13e5c3ad939"
SRCREV_libcxx = "a80de495f78131863bcbcb722e703e0623bfeb99"
SRCREV_libunwind = "878c40b2f240ee9f57be7dce86caf7650f79dc7f"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
