# meta-clang (a C language family frontend and LLVM compiler backend)

This layer provides [clang/llvm](http://clang.llvm.org/) as alternative to your system
C/C++ compiler for OpenEmbedded based distributions along with gcc

# Getting Started

```shell
git clone git://github.com/openembedded/openembedded-core.git
cd openembeeded-core
git clone git://github.com/openembedded/bitbake.git
git clone git://github.com/kraj/meta-clang.git

$ . ./oe-init-build-env
```

Edit conf/bblayers.conf to add meta-clang to layer mix e.g.

```python
BBLAYERS ?= " \
  /home/kraj/openembedded-core/meta-clang \
  /home/kraj/openembedded-core/meta \
  "
```

# Building

Below we build for qemuarm machine as an example

```shell
$ MACHINE=qemux86 bitbake core-image-minimal
```
# Running

```shell
$ runqemu qemux86
```

# Limitations

Currently only few components do not build with clang, if you have a component to add to that list
simply create a bbappend under recipes-excluded/nonclangable e.g.

```shell
TOOLCHAIN = "gcc"
```

and OE will start using gcc to cross compile that recipe, please note that by default when meta-clang is in layermix
it will use clang as default system compiler, if you would like to disable that behaviour please add

```shell
TOOLCHAIN = "gcc"
```
to your local.conf or some other global configuration metadata file.

# Dependencies

```
URI: git://github.com/openembedded/openembedded-core.git
branch: master
revision: HEAD

URI: git://github.com/openembedded/bitbake.git
branch: master
revision: HEAD
```

Send pull requests to openembedded-devel@lists.openembedded.org with '[meta-clang]' in the subject'

When sending single patches, please use something like:

'git send-email -M -1 --to openembedded-devel@lists.openembedded.org --subject-prefix=meta-clang][PATCH'

You are encouraged to fork the mirror on [github](https://github.com/kraj/meta-clang/)
to share your patches, this is preferred for patch sets consisting of more than 
one patch. Other services like gitorious, repo.or.cz or self hosted setups are 
of course accepted as well, 'git fetch <remote>' works the same on all of them.
We recommend github because it is free, easy to use, has been proven to be reliable 
and has a really good web GUI.

Layer Maintainer: Khem Raj <mailto:raj.khem@gmail.com>
