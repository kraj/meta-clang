LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.1"
BRANCH = "release_39"
SRCREV_llvm = "46079999e9111e8e8c2c1f0511477c3bec38d74a"
SRCREV_clang = "c3709e72d22432f53f8e2f14354def31a96734fe"
SRCREV_compiler-rt = "38631afc1dec4d5b13b6460e808b5dae6c009081"
SRCREV_cxxabi = "47803aa110d52c542c7a116b5b40daab7265fd27"
SRCREV_libcxx = "ff4c6a0a9eff9b3ad77418bec839bec723da7754"
SRCREV_libunwind = "63ed8dc8e4a73efd7d46b6cb79453bb428eb138d"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
