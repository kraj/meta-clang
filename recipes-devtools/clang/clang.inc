LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "3.9.1"
BRANCH = "release_39"
SRCREV_llvm = "a093ef43dd592b729da46db4ff3057fef9a46023"
SRCREV_clang = "54f5752c3600d39ee8de62ba9ff304154baf5e80"
SRCREV_compiler-rt = "9822763f9403c0740a01b7b5b50e3f15eec76b65"
SRCREV_cxxabi = "47803aa110d52c542c7a116b5b40daab7265fd27"
SRCREV_libcxx = "1ebab9ea93499e2d20eeb64d57a81af8d7d36772"
SRCREV_libunwind = "63ed8dc8e4a73efd7d46b6cb79453bb428eb138d"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
