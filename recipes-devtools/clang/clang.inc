LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.0"
BRANCH = "release_39"
SRCREV_llvm = "0316f025dab46d776dee70d3b695e1a07db5371c"
SRCREV_clang = "07307f95d5c82d453cdc5c23f9ccd53d5ff75426"
SRCREV_compiler-rt = "96f4e6709e2a5192576b0467fe3d7ce6dc646b38"
SRCREV_cxxabi = "47803aa110d52c542c7a116b5b40daab7265fd27"
SRCREV_libcxx = "ff4c6a0a9eff9b3ad77418bec839bec723da7754"
SRCREV_libunwind = "6f12065c0fc09ebb0d8060d7f184f14e3599e621"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
