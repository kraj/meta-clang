LLVM_RELEASE = ""
LLVM_DIR = "llvm${LLVM_RELEASE}"

LLVM_GIT ?= "git://github.com/llvm-mirror"
LLVM_GIT_PROTOCOL ?= "git"

#
# Uncomment below to enable master version of clang/llvm
#

PV = "4.0.0"
BRANCH = "release_40"
SRCREV_llvm = "80d55d0a78b03a6384b0364f243c645d1a57e723"
SRCREV_clang = "2574fd71cbf243821653ec56d4818bc48c2f4be7"
SRCREV_compiler-rt = "850646edf7e605354c66693c16ab69193e04a078"
SRCREV_cxxabi = "0e46ed1f6c22968c91337aa918cdd7dc302829cb"
SRCREV_libcxx = "39c36254e55627b5f94d37a453bf97fcd907cd38"
SRCREV_libunwind = "9f88e323c3d0926f52ab3d4a5379eefd92ff28c2"

LLVMMD5SUM = "b99eb43c934ceebecab85c6b9b1a08be"
CLANGMD5SUM = "a77eac638a3aae44a2d604217d6f0f01"
