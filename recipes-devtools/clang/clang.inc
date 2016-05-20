LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "e3282d30d89c09aeae6af1a43556ffed62d24ddb"
SRCREV_clang = "3e258a6c0fc66ab33b992bda96fbc7dc2e779089"
SRCREV_compiler-rt = "8ff78a0876137bd2b2a980ddb6d77abfc2c178d1"
SRCREV_libcxxabi = "5985dd69c9ef90f3eb13658253bd7800c16db4e1"
SRCREV_libcxx = "14273e8a560533f44238b5ec6299a8edcedf2144"
SRCREV_libunwind = "7b1a88cf2a276f26d7f22c09f05accd3c750ca35"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
