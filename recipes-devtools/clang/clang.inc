LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "8340aef947a5e797ff2dad4abc6f544049041474"
SRCREV_clang = "1a21ba3207012ae792e467a2568a8ff52c2c590c"
SRCREV_compiler-rt = "d4f13ca07b5d463732187c9e3d15653cdf88e2f5"
SRCREV_cxxabi = "e4b123bead9064fe408ebb7bcf261214701ef2db"
SRCREV_libcxx = "4414a6a91122ed02d42745e14ebefb116e5bf773"
SRCREV_libunwind = "122a0f8114e58a294ee3b2bb1d4f9537ef7ed6df"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
