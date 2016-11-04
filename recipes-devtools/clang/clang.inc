LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "4373dd0316a6d3fa4572bfa9d1fbd9f83b80b865"
SRCREV_clang = "6fdc22eec34fd2b3c5cb5e0da21f6126d7fed9bd"
SRCREV_compiler-rt = "692b01cdac57043f8a69f5943142266a63cb721d"
SRCREV_cxxabi = "c320e4c36c06bc0019fc06eaf00eb064727cfcc0"
SRCREV_libcxx = "f333beee2ce44299b1ce219371bdef60290ac175"
SRCREV_libunwind = "0eea5e270b455f79c59df69b8fb38f68399314a2"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
