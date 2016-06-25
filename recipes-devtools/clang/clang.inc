LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "d207fc4c12d5a4a2660fb925858811544119a07a"
SRCREV_clang = "116ce8458879ac0721cfd7badfd4c3868ed2da78"
SRCREV_compiler-rt = "8ec81ae4bda307e683f25ffd7b2caf2d65aa18ad"
SRCREV_libcxxabi = "5328c6b635a145efc1204ad25341706fb705e346"
SRCREV_libcxx = "dd421f5acd9016a2f88a09150904af463ad17815"
SRCREV_libunwind = "7a00ec9517645046685fd6c115f0b1dc775f0bb3"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
