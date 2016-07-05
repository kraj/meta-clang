LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "ae50a144ace99690fb530a2d56b625ac3cb13845"
SRCREV_clang = "bf0295e9294348694b7b203149fb108be098c56c"
SRCREV_compiler-rt = "0ef859b2aaaaf976fb32d0e6c6a28cfecb57a1e7"
SRCREV_libcxxabi = "5328c6b635a145efc1204ad25341706fb705e346"
SRCREV_libcxx = "56a761493d40cd528d548dfbf5fb27fa37a92556"
SRCREV_libunwind = "7a00ec9517645046685fd6c115f0b1dc775f0bb3"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
