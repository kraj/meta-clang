LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "release_39"
SRCREV_llvm = "55ea065c2eab83335d33478009d547f4563465e8"
SRCREV_clang = "959e1a652e93707bbbe2d6134953257ebe904360"
SRCREV_compiler-rt = "85cd828f87c0800e698b8d62334b2e1270375307"
SRCREV_cxxabi = "03b9b3ddde45263af678302f36788ee6bb7a9c25"
SRCREV_libcxx = "95fa236b0059899e96b4092e66e776608c4e3c52"
SRCREV_libunwind = "6f12065c0fc09ebb0d8060d7f184f14e3599e621"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
