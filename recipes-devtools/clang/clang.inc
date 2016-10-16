LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "e0043a340f7f9f64a16859279348dd09eab346b4"
SRCREV_clang = "fce476b6e965594e2bf2ea2f03a3d1021e2e227a"
SRCREV_compiler-rt = "d9e6c738682c2012e5e7e87909941becd55cfbbd"
SRCREV_cxxabi = "8ac10e4bb995075208e8d6b99e03e9f315d0f5a3"
SRCREV_libcxx = "34c6b805cf0358809ea95c0d4303537be3225db7"
SRCREV_libunwind = "122a0f8114e58a294ee3b2bb1d4f9537ef7ed6df"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
