LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "master"
SRCREV_llvm = "9c5e4bac4a745f6a621dcf2803fa89f6bbe51862"
SRCREV_clang = "3b61b92e0fdf5093a247270ef1265c85697de3f9"
SRCREV_compiler-rt = "f2de077dc7d89466b630c308ed8eaf6bb4915210"
SRCREV_cxxabi = "802f4f4a49ed7a3ecec2461f0009c10b985fba73"
SRCREV_libcxx = "d842fed6f8bb52516e24b751cfc5c5f8ca1049c3"
SRCREV_libunwind = "8bfcea40f9973dadfaafae59192cd45b616e605b"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
