LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "master"
SRCREV_llvm = "b7ebc01945b9efb5efffd77cb7a7f6dd3ab72dd5"
SRCREV_clang = "56fdf6c411068bbc01fcd802eddb489c79bf4852"
SRCREV_compiler-rt = "97a35f8a8e9741e9157c0afdbb48a9db99d8a718"
SRCREV_libcxxabi = "6fd0d958b55fbbefeb5e1fb78cdc086eddb92a43"
SRCREV_libcxx = "d8df1781e3e0041d81899c9131b43c6655c75351"
SRCREV_libunwind = "7b1a88cf2a276f26d7f22c09f05accd3c750ca35"

LLVMMD5SUM = "43fdaa303c1c5589ad60f4ffc6a0b9ce"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
