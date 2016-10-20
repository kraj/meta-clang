LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "f43c9c6d35ec9669a320cdb97e408bcce938b94d"
SRCREV_clang = "3d7fceb69020c5d6d9dda4b52cde9b4ece620431"
SRCREV_compiler-rt = "d4f13ca07b5d463732187c9e3d15653cdf88e2f5"
SRCREV_cxxabi = "e4b123bead9064fe408ebb7bcf261214701ef2db"
SRCREV_libcxx = "4414a6a91122ed02d42745e14ebefb116e5bf773"
SRCREV_libunwind = "122a0f8114e58a294ee3b2bb1d4f9537ef7ed6df"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
