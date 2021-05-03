# How to contribute

I'm really glad you're reading this, because we need developers who can write good code which is maintainable and
extendable not only by them but by others equally well. This document outlines basic conventions to be followed when
coding in this project.

## Branch Naming

Branches are required to develop new features or enhancements or to fix bugs. We follow mainly trunk based development
for most of our work. Following are some conventions to follow while naming branches:

- **main** _[Base]_ _[Locked]_ - Used as the branch where code is merged after development and is under testing.
- **release/\*** - These are branches which are formed from **main** for releasing a piece of code to production.
- **feature/\*** - These represent full-fledged features or major enhancements which are in development.
- **enhancements/\*** - These represent minor enhancements which are in development
- **bugs/\*** - These are the branches created to fix some bug and should not exist in repository for long
- **tmp/\*** - These will represent experimental features, which may or may not materialize

Apart from above branches we also use tags for maintaining releases in the system permanently. Release tags will be 
named as **release-***

## Submitting Changes

For development you'll create a new branch from **main** under the relevant category of work. After work is done, all
that code needs to be merged back into **main**. For doing so, it needs to go through a review process, and here is how
we go about it:

- Create a pull request (PR) for your branch with **main** as base branch
- Provide description of your PR explaining your approach towards the code. It doesn't need to be very detailed, a brief
  description is enough
- Assign it to someone on the team for them to review
- During a review you may be given suggestions on code improvement, update your code accordingly
- Once review is successfully completed, code will be merged into **main** by the reviewer

## Merge Conflicts

A very common problem that you encounter while working with Git are merge conflicts. In a nutshell, a merge conflict is
when git while merging code from two branches encounter that same line of code has been changed and can't decide which
one to keep and which one to discard

Use tools that help you see code differences in a graphical interface, like Meld. If you are not confident that you can
fully merge the code yourself, seek assistance and learn how to go about merging the code.

## Contributors

- [Rohit Aggarwal](mailto:er.rohitaggarwal1989@gmail.com)

### Note

More guidelines and conventions will be added to this page every now and then, so make sure you are going through them
every once in a while.

**!!! A good code makes a good developer !!!**
