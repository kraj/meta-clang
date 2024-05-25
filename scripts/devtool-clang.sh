#!/usr/bin/env sh
# Clone the repository first if not already done locally
# git clone https://github.com/kraj/llvm-project -b oe/main /mnt/b/yoe/master/workspace/sources/llvm-project
#
# if local repository exists then make a clone/copy
# git clone /home/kraj/work/llvm-project /mnt/b/yoe/master/workspace/sources/llvm-project
#
layerloc="$(dirname "$0")/../conf/layer.conf"
workspace="$(dirname "$0")/../../../workspace"

origver=$(grep "LLVMVERSION =" < "$layerloc" | awk '{print $3}' | tr -d '"')

major=$(grep -e "set(LLVM_VERSION_MAJOR [0-9]" < "$workspace"/sources/llvm-project/cmake/Modules/LLVMVersion.cmake| cut -d ' ' -f 4 | sed "s/)//")
minor=$(grep -e "set(LLVM_VERSION_MINOR [0-9]" < "$workspace"/sources/llvm-project/cmake/Modules/LLVMVersion.cmake| cut -d ' ' -f 4 | sed "s/)//")
patch=$(grep -e "set(LLVM_VERSION_PATCH [0-9]" < "$workspace"/sources/llvm-project/cmake/Modules/LLVMVersion.cmake| cut -d ' ' -f 4 | sed "s/)//")

recipes="\
llvm-project-source-$origver \
clang \
clang-cross-riscv64 \
clang-crosssdk-x86_64 \
clang-cross-canadian-riscv64 \
nativesdk-clang-glue \
compiler-rt \
compiler-rt-sanitizers \
libclc \
libcxx \
openmp \
"

for recipe in $recipes; do
  devtool modify -n "$recipe" "$workspace/sources/llvm-project"
  sed -i "/pn-$recipe /p;s/pn-$recipe /pn-nativesdk-$recipe /g" "$workspace"/appends/"$recipe"*.bbappend
  sed -i "/pn-$recipe /p;s/pn-$recipe /pn-$recipe-native /g" "$workspace"/appends/"$recipe"*.bbappend
done

for f in "$workspace"/appends/*.bbappend; do
  { echo "MAJOR_VER = \"$major\"" ; echo "MINOR_VER = \"$minor\"" ; echo "PATCH_VER = \"$patch\"" ; } >> "$f"
done

sed -i -e "s/$origver/$major.$minor.$patch/g" "$workspace"/appends/llvm-project-source.bbappend
