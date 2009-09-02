begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *  Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements. See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership. The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License. You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied. See the License for the  *  specific language governing permissions and limitations  *  under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStreamReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_comment
comment|/* dkulp - Stupid little program I use to help merge changes from     trunk to the fixes branches.   It requires the svnmerge.py be     available on the path.   Grab the latest from:    http://svn.collab.net/repos/svn/trunk/contrib/client-side/svnmerge/    (of course, that then requries python installed and whatever else svnmerge.py    needs.)   It also requires the command line version of svn.     Basically, svnmerge.py does all the work, but this little wrapper     thing will display the commit logs, prompt if you want to merge/block/ignore    each commit, prompt for commit (so you can resolve any conflicts first),     etc....     Yes - doing this in python itself (or perl or even bash itself or ruby or ...)     would probably be better.  However, I'd then need to spend time     learning python/ruby/etc... that I just don't have time to do right now.    What is more productive: Taking 30 minutes to bang this out in Java or    spending a couple days learning another language that would allow me to    bang it out in 15 minutes? */
end_comment

begin_class
specifier|public
class|class
name|DoMerges
block|{
specifier|public
specifier|static
name|boolean
name|auto
init|=
literal|false
decl_stmt|;
specifier|static
name|void
name|doCommit
parameter_list|()
throws|throws
name|Exception
block|{
while|while
condition|(
name|System
operator|.
name|in
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|in
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|char
name|c
init|=
name|auto
condition|?
literal|'Y'
else|:
literal|0
decl_stmt|;
while|while
condition|(
name|c
operator|!=
literal|'Y'
operator|&&
name|c
operator|!=
literal|'N'
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"Commit:  [Y]es, or [N]o? "
argument_list|)
expr_stmt|;
name|int
name|i
init|=
name|System
operator|.
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
name|c
operator|=
name|Character
operator|.
name|toUpperCase
argument_list|(
operator|(
name|char
operator|)
name|i
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|c
operator|==
literal|'N'
condition|)
block|{
return|return;
block|}
name|Process
name|p
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"svn"
block|,
literal|"resolved"
block|,
literal|"."
block|}
argument_list|)
decl_stmt|;
if|if
condition|(
name|p
operator|.
name|waitFor
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|10
argument_list|)
expr_stmt|;
block|}
name|p
operator|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"svn"
block|,
literal|"commit"
block|,
literal|"-F"
block|,
literal|"svnmerge-commit-message.txt"
block|}
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|waitFor
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"ERROR!"
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getErrorStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
name|args
index|[]
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|args
operator|.
name|length
operator|>
literal|0
operator|&&
literal|"-auto"
operator|.
name|equals
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|auto
operator|=
literal|true
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Updating directory"
argument_list|)
expr_stmt|;
name|Process
name|p
init|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"svn"
block|,
literal|"up"
block|,
literal|"-r"
block|,
literal|"head"
block|,
literal|"."
block|}
argument_list|)
decl_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|line
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|p
operator|.
name|waitFor
argument_list|()
expr_stmt|;
name|p
operator|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
name|getCommandLine
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"svnmerge.py"
block|,
literal|"avail"
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|verList
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|String
name|vers
index|[]
init|=
name|line
operator|.
name|split
argument_list|(
literal|","
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|vers
control|)
block|{
if|if
condition|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"-"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|String
name|s1
init|=
name|s
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|s
operator|.
name|indexOf
argument_list|(
literal|"-"
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|s2
init|=
name|s
operator|.
name|substring
argument_list|(
name|s
operator|.
name|indexOf
argument_list|(
literal|"-"
argument_list|)
operator|+
literal|1
argument_list|)
decl_stmt|;
name|int
name|i1
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s1
argument_list|)
decl_stmt|;
name|int
name|i2
init|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|s2
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
name|i1
init|;
name|x
operator|<=
name|i2
condition|;
name|x
operator|++
control|)
block|{
name|verList
operator|.
name|add
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|x
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|verList
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|p
operator|.
name|waitFor
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Merging versions ("
operator|+
name|verList
operator|.
name|size
argument_list|()
operator|+
literal|"): "
operator|+
name|verList
argument_list|)
expr_stmt|;
name|String
name|root
init|=
literal|null
decl_stmt|;
name|p
operator|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"svn"
block|,
literal|"info"
block|}
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|line
operator|.
name|startsWith
argument_list|(
literal|"Repository Root: "
argument_list|)
condition|)
block|{
name|root
operator|=
name|line
operator|.
name|substring
argument_list|(
literal|"Repository Root: "
operator|.
name|length
argument_list|()
argument_list|)
operator|.
name|trim
argument_list|()
expr_stmt|;
block|}
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Root: "
operator|+
name|root
argument_list|)
expr_stmt|;
name|p
operator|.
name|waitFor
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|blocks
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|int
name|count
init|=
literal|1
decl_stmt|;
for|for
control|(
name|String
name|ver
range|:
name|verList
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Merging: "
operator|+
name|ver
operator|+
literal|" ("
operator|+
operator|(
name|count
operator|++
operator|)
operator|+
literal|"/"
operator|+
name|verList
operator|.
name|size
argument_list|()
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|p
operator|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"svn"
block|,
literal|"log"
block|,
literal|"-r"
block|,
name|ver
block|,
name|root
block|}
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|p
operator|.
name|waitFor
argument_list|()
expr_stmt|;
while|while
condition|(
name|System
operator|.
name|in
operator|.
name|available
argument_list|()
operator|>
literal|0
condition|)
block|{
name|System
operator|.
name|in
operator|.
name|read
argument_list|()
expr_stmt|;
block|}
name|char
name|c
init|=
name|auto
condition|?
literal|'M'
else|:
literal|0
decl_stmt|;
while|while
condition|(
name|c
operator|!=
literal|'M'
operator|&&
name|c
operator|!=
literal|'B'
operator|&&
name|c
operator|!=
literal|'I'
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|print
argument_list|(
literal|"[M]erge, [B]lock, or [I]gnore? "
argument_list|)
expr_stmt|;
name|int
name|i
init|=
name|System
operator|.
name|in
operator|.
name|read
argument_list|()
decl_stmt|;
name|c
operator|=
name|Character
operator|.
name|toUpperCase
argument_list|(
operator|(
name|char
operator|)
name|i
argument_list|)
expr_stmt|;
block|}
switch|switch
condition|(
name|c
condition|)
block|{
case|case
literal|'M'
case|:
name|p
operator|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
name|getCommandLine
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"svnmerge.py"
block|,
literal|"merge"
block|,
literal|"-r"
block|,
name|ver
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|waitFor
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"ERROR!"
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getErrorStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|doCommit
argument_list|()
expr_stmt|;
break|break;
case|case
literal|'B'
case|:
name|blocks
operator|.
name|add
argument_list|(
name|ver
argument_list|)
expr_stmt|;
break|break;
case|case
literal|'I'
case|:
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Ignoring"
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|blocks
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|StringBuilder
name|ver
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|blocks
control|)
block|{
if|if
condition|(
name|ver
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|ver
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
block|}
name|ver
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Blocking "
operator|+
name|ver
argument_list|)
expr_stmt|;
name|p
operator|=
name|Runtime
operator|.
name|getRuntime
argument_list|()
operator|.
name|exec
argument_list|(
name|getCommandLine
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"svnmerge.py"
block|,
literal|"block"
block|,
literal|"-r"
block|,
name|ver
operator|.
name|toString
argument_list|()
block|}
argument_list|)
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getInputStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|p
operator|.
name|waitFor
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"ERROR!"
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|p
operator|.
name|getErrorStream
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
while|while
condition|(
name|line
operator|!=
literal|null
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|line
argument_list|)
expr_stmt|;
name|line
operator|=
name|reader
operator|.
name|readLine
argument_list|()
expr_stmt|;
block|}
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|doCommit
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|String
index|[]
name|getCommandLine
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|argLine
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|isWindows
argument_list|()
condition|)
block|{
name|argLine
operator|.
name|add
argument_list|(
literal|"cmd.exe"
argument_list|)
expr_stmt|;
name|argLine
operator|.
name|add
argument_list|(
literal|"/c"
argument_list|)
expr_stmt|;
block|}
name|argLine
operator|.
name|addAll
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|args
argument_list|)
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Running "
operator|+
name|argLine
operator|+
literal|"..."
argument_list|)
expr_stmt|;
return|return
name|argLine
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|argLine
operator|.
name|size
argument_list|()
index|]
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|boolean
name|isWindows
parameter_list|()
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
literal|"os.name"
argument_list|)
operator|.
name|toLowerCase
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"windows"
argument_list|)
operator|!=
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit

