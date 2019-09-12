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

# Default Compiler Switch

Note that by default gcc will remain the system compiler, however if you wish
clang to be the default compiler then set

```python
TOOLCHAIN ?= "clang"
```

in local.conf, this would now switch to using clang as default compiler systemwide
you can select clang per package too by writing bbappends for them containing

```python
TOOLCHAIN = "clang"
```

# Default C++ Standard Library Switch

Note that by default libstdc++ will remain the default C++ standard library, however if you wish
libc++ to be the default one then set

```python
TARGET_CXXFLAGS_append_toolchain-clang ?= " -stdlib=libc++ "
```

in local.conf.
You can select libc++ per package too by writing bbappends for them containing

```python
TARGET_CXXFLAGS_append_toolchain-clang = " -stdlib=libc++ "
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

Few components do not build with clang, if you have a component to add to that list
simply add it to conf/nonclangable.inc e.g.

```shell
TOOLCHAIN_pn-<recipe> = "gcc"
```

and OE will start using gcc to cross compile that recipe.

And if a component does not build with libc++, you can add it to conf/nonclangable.inc e.g.

```shell
TARGET_CXXFLAGS_remove_pn-<recipe>_toolchain-clang = " -stdlib=libc++ "
```
# Classic Cross-Compile SDK Use With CMake and Autotools

The classic cross-compilation SDK from Yocto uses the GCC specified in the GCCVERSION that is chosen by Yocto at build time by default.  There are six total environment variables set by Yocto when you stage up the Classic SDK for use:

    CC
    CXX
    CPP
    CLANGCC
    CLANGCXX
    CLANGCPP

The first three contain GCC specific configurations that work correctly for the GCC version in the SDK  These will work correctly to build with the GCC cross-compilation toolchain with CMake and Autotools out of the box.  If you want to make clang available to be used the same way, you only need to copy the contents of the CLANG env vars over to their respective "GCC" equivalents (e.g. CC=$CLANGCC).  Once done, CMake and Autotools will correctly work with clang instead.

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

Layer Maintainer: [Khem Raj](<mailto:raj.khem@gmail.com>)
