begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|testutil
operator|.
name|common
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URLClassLoader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_class
specifier|public
class|class
name|ServerLauncher
block|{
specifier|public
specifier|static
specifier|final
name|int
name|DEFAULT_TIMEOUT
init|=
literal|3
operator|*
literal|60
operator|*
literal|1000
decl_stmt|;
specifier|protected
specifier|static
specifier|final
name|String
name|SERVER_FAILED
init|=
literal|"server startup failed (not a log message)"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|boolean
name|DEFAULT_IN_PROCESS
init|=
literal|false
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|ServerLauncher
operator|.
name|class
argument_list|)
decl_stmt|;
name|boolean
name|serverPassed
decl_stmt|;
specifier|final
name|String
name|className
decl_stmt|;
specifier|private
specifier|final
name|boolean
name|debug
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|inProcess
init|=
name|DEFAULT_IN_PROCESS
decl_stmt|;
specifier|private
name|AbstractTestServerBase
name|inProcessServer
decl_stmt|;
specifier|private
specifier|final
name|String
name|javaExe
decl_stmt|;
specifier|private
name|Process
name|process
decl_stmt|;
specifier|private
name|boolean
name|serverIsReady
decl_stmt|;
specifier|private
name|boolean
name|serverIsStopped
decl_stmt|;
specifier|private
name|boolean
name|serverLaunchFailed
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
decl_stmt|;
specifier|private
name|String
index|[]
name|serverArgs
decl_stmt|;
specifier|private
specifier|final
name|Mutex
name|mutex
init|=
operator|new
name|Mutex
argument_list|()
decl_stmt|;
specifier|public
name|ServerLauncher
parameter_list|(
name|String
name|theClassName
parameter_list|)
block|{
name|this
argument_list|(
name|theClassName
argument_list|,
name|DEFAULT_IN_PROCESS
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ServerLauncher
parameter_list|(
name|AbstractTestServerBase
name|b
parameter_list|)
block|{
name|inProcess
operator|=
literal|true
expr_stmt|;
name|inProcessServer
operator|=
name|b
expr_stmt|;
name|javaExe
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
literal|"bin"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"java"
expr_stmt|;
name|className
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|ServerLauncher
parameter_list|(
name|String
name|theClassName
parameter_list|,
name|boolean
name|inprocess
parameter_list|)
block|{
name|inProcess
operator|=
name|inprocess
expr_stmt|;
name|className
operator|=
name|theClassName
expr_stmt|;
name|javaExe
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
literal|"bin"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"java"
expr_stmt|;
block|}
specifier|public
name|ServerLauncher
parameter_list|(
name|String
name|theClassName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
parameter_list|,
name|String
index|[]
name|args
parameter_list|)
block|{
name|this
argument_list|(
name|theClassName
argument_list|,
name|p
argument_list|,
name|args
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ServerLauncher
parameter_list|(
name|String
name|theClassName
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|p
parameter_list|,
name|String
index|[]
name|args
parameter_list|,
name|boolean
name|inprocess
parameter_list|)
block|{
name|className
operator|=
name|theClassName
expr_stmt|;
name|properties
operator|=
name|p
expr_stmt|;
name|serverArgs
operator|=
name|args
expr_stmt|;
name|inProcess
operator|=
name|inprocess
expr_stmt|;
name|javaExe
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.home"
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
literal|"bin"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"java"
expr_stmt|;
block|}
specifier|private
name|boolean
name|waitForServerToStop
parameter_list|()
block|{
synchronized|synchronized
init|(
name|mutex
init|)
block|{
while|while
condition|(
operator|!
name|serverIsStopped
condition|)
block|{
try|try
block|{
name|TimeoutCounter
name|tc
init|=
operator|new
name|TimeoutCounter
argument_list|(
name|DEFAULT_TIMEOUT
argument_list|)
decl_stmt|;
name|mutex
operator|.
name|wait
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
if|if
condition|(
name|tc
operator|.
name|isTimeoutExpired
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"destroying server process"
argument_list|)
expr_stmt|;
name|process
operator|.
name|destroy
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|//ex.printStackTrace();
block|}
block|}
if|if
condition|(
operator|!
name|inProcess
condition|)
block|{
comment|//wait for process to end...
name|TimeoutCounter
name|tc
init|=
operator|new
name|TimeoutCounter
argument_list|(
name|DEFAULT_TIMEOUT
argument_list|)
decl_stmt|;
while|while
condition|(
operator|!
name|tc
operator|.
name|isTimeoutExpired
argument_list|()
condition|)
block|{
try|try
block|{
name|process
operator|.
name|exitValue
argument_list|()
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|IllegalThreadStateException
name|ex
parameter_list|)
block|{
comment|//ignore, process hasn't ended
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex1
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
if|if
condition|(
name|tc
operator|.
name|isTimeoutExpired
argument_list|()
condition|)
block|{
name|process
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|return
name|serverIsStopped
return|;
block|}
specifier|public
name|void
name|signalStop
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|process
operator|!=
literal|null
condition|)
block|{
name|process
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
literal|'q'
argument_list|)
expr_stmt|;
name|process
operator|.
name|getOutputStream
argument_list|()
operator|.
name|write
argument_list|(
literal|'\n'
argument_list|)
expr_stmt|;
name|process
operator|.
name|getOutputStream
argument_list|()
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|boolean
name|stopServer
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|inProcess
condition|)
block|{
try|try
block|{
return|return
name|inProcessServer
operator|.
name|stopInProcess
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
else|else
block|{
if|if
condition|(
name|process
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|serverIsStopped
condition|)
block|{
try|try
block|{
name|signalStop
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
comment|//ignore
block|}
block|}
name|waitForServerToStop
argument_list|()
expr_stmt|;
name|process
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|serverPassed
return|;
block|}
specifier|public
name|boolean
name|launchServer
parameter_list|()
throws|throws
name|IOException
block|{
name|serverIsReady
operator|=
literal|false
expr_stmt|;
name|serverLaunchFailed
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|inProcess
condition|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|old
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
try|try
block|{
if|if
condition|(
literal|null
operator|!=
name|properties
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|old
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|System
operator|.
name|getProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|clearProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|inProcessServer
operator|==
literal|null
condition|)
block|{
name|cls
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|className
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
extends|extends
name|AbstractTestServerBase
argument_list|>
name|svcls
init|=
name|cls
operator|.
name|asSubclass
argument_list|(
name|AbstractTestServerBase
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|serverArgs
condition|)
block|{
name|inProcessServer
operator|=
name|svcls
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Constructor
argument_list|<
name|?
extends|extends
name|AbstractTestServerBase
argument_list|>
name|ctor
init|=
name|svcls
operator|.
name|getConstructor
argument_list|(
name|serverArgs
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|inProcessServer
operator|=
name|ctor
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
name|serverArgs
block|}
argument_list|)
expr_stmt|;
block|}
block|}
name|inProcessServer
operator|.
name|startInProcess
argument_list|()
expr_stmt|;
name|serverIsReady
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|serverLaunchFailed
operator|=
literal|true
expr_stmt|;
block|}
finally|finally
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|old
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|entry
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|)
block|{
name|System
operator|.
name|clearProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|setProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|List
argument_list|<
name|String
argument_list|>
name|cmd
decl_stmt|;
try|try
block|{
name|cmd
operator|=
name|getCommand
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e1
parameter_list|)
block|{
name|IOException
name|ex
init|=
operator|new
name|IOException
argument_list|()
decl_stmt|;
name|ex
operator|.
name|initCause
argument_list|(
name|e1
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"CMD: "
operator|+
name|cmd
argument_list|)
expr_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|"CMD: "
operator|+
name|cmd
argument_list|)
expr_stmt|;
block|}
name|ProcessBuilder
name|pb
init|=
operator|new
name|ProcessBuilder
argument_list|(
name|cmd
argument_list|)
decl_stmt|;
name|pb
operator|.
name|redirectErrorStream
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|process
operator|=
name|pb
operator|.
name|start
argument_list|()
expr_stmt|;
name|OutputMonitorThread
name|out
init|=
name|launchOutputMonitorThread
argument_list|(
name|process
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|System
operator|.
name|out
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|mutex
init|)
block|{
do|do
block|{
name|TimeoutCounter
name|tc
init|=
operator|new
name|TimeoutCounter
argument_list|(
name|DEFAULT_TIMEOUT
argument_list|)
decl_stmt|;
try|try
block|{
name|mutex
operator|.
name|wait
argument_list|(
literal|1000
argument_list|)
expr_stmt|;
if|if
condition|(
name|tc
operator|.
name|isTimeoutExpired
argument_list|()
condition|)
block|{
break|break;
block|}
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
do|while
condition|(
operator|!
name|serverIsReady
operator|&&
operator|!
name|serverLaunchFailed
condition|)
do|;
block|}
if|if
condition|(
name|serverLaunchFailed
operator|||
operator|!
name|serverIsReady
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
name|out
operator|.
name|getServerOutput
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|serverIsReady
operator|&&
operator|!
name|serverLaunchFailed
return|;
block|}
specifier|public
name|int
name|waitForServer
parameter_list|()
block|{
name|int
name|ret
init|=
operator|-
literal|1
decl_stmt|;
try|try
block|{
name|process
operator|.
name|waitFor
argument_list|()
expr_stmt|;
name|ret
operator|=
name|process
operator|.
name|exitValue
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
name|ret
return|;
block|}
specifier|private
name|OutputMonitorThread
name|launchOutputMonitorThread
parameter_list|(
specifier|final
name|InputStream
name|in
parameter_list|,
specifier|final
name|PrintStream
name|out
parameter_list|)
block|{
name|OutputMonitorThread
name|t
init|=
operator|new
name|OutputMonitorThread
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
decl_stmt|;
name|t
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|t
return|;
block|}
specifier|private
class|class
name|OutputMonitorThread
extends|extends
name|Thread
block|{
name|InputStream
name|in
decl_stmt|;
name|PrintStream
name|out
decl_stmt|;
name|StringBuilder
name|serverOutputAll
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|OutputMonitorThread
parameter_list|(
name|InputStream
name|i
parameter_list|,
name|PrintStream
name|o
parameter_list|)
block|{
name|in
operator|=
name|i
expr_stmt|;
name|out
operator|=
name|o
expr_stmt|;
block|}
specifier|public
name|String
name|getServerOutput
parameter_list|()
block|{
return|return
name|serverOutputAll
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|String
name|outputDir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"server.output.dir"
argument_list|,
literal|"target/surefire-reports/"
argument_list|)
decl_stmt|;
name|FileOutputStream
name|fos
decl_stmt|;
try|try
block|{
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|outputDir
operator|+
name|className
operator|+
literal|".out"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|fex
parameter_list|)
block|{
name|outputDir
operator|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
expr_stmt|;
if|if
condition|(
name|outputDir
operator|==
literal|null
condition|)
block|{
name|outputDir
operator|=
literal|"target/surefire-reports/"
expr_stmt|;
block|}
else|else
block|{
name|outputDir
operator|+=
literal|"/target/surefire-reports/"
expr_stmt|;
block|}
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|outputDir
argument_list|)
decl_stmt|;
name|file
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|fos
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|outputDir
operator|+
name|className
operator|+
literal|".out"
argument_list|)
expr_stmt|;
block|}
name|PrintStream
name|ps
init|=
operator|new
name|PrintStream
argument_list|(
name|fos
argument_list|)
decl_stmt|;
name|boolean
name|running
init|=
literal|true
decl_stmt|;
name|StringBuilder
name|serverOutput
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|ch
init|=
name|in
operator|.
name|read
argument_list|()
init|;
name|ch
operator|!=
operator|-
literal|1
condition|;
name|ch
operator|=
name|in
operator|.
name|read
argument_list|()
control|)
block|{
name|serverOutput
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|ch
argument_list|)
expr_stmt|;
name|serverOutputAll
operator|.
name|append
argument_list|(
operator|(
name|char
operator|)
name|ch
argument_list|)
expr_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
operator|(
name|char
operator|)
name|ch
argument_list|)
expr_stmt|;
block|}
name|String
name|s
init|=
name|serverOutput
operator|.
name|toString
argument_list|()
decl_stmt|;
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
literal|"server ready"
argument_list|)
condition|)
block|{
name|notifyServerIsReady
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
literal|"server passed"
argument_list|)
condition|)
block|{
name|serverPassed
operator|=
literal|true
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
literal|"server stopped"
argument_list|)
condition|)
block|{
name|notifyServerIsStopped
argument_list|()
expr_stmt|;
name|running
operator|=
literal|false
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|s
operator|.
name|contains
argument_list|(
name|SERVER_FAILED
argument_list|)
condition|)
block|{
name|notifyServerFailed
argument_list|()
expr_stmt|;
name|running
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|ch
operator|==
literal|'\n'
operator|||
operator|!
name|running
condition|)
block|{
synchronized|synchronized
init|(
name|out
init|)
block|{
name|ps
operator|.
name|print
argument_list|(
name|serverOutput
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|serverOutput
operator|.
name|setLength
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ps
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
block|}
name|ps
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
if|if
condition|(
operator|!
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|contains
argument_list|(
literal|"Stream closed"
argument_list|)
condition|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
name|void
name|notifyServerIsReady
parameter_list|()
block|{
synchronized|synchronized
init|(
name|mutex
init|)
block|{
name|serverIsReady
operator|=
literal|true
expr_stmt|;
name|mutex
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
name|void
name|notifyServerIsStopped
parameter_list|()
block|{
synchronized|synchronized
init|(
name|mutex
init|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"notify server stopped"
argument_list|)
expr_stmt|;
name|serverIsStopped
operator|=
literal|true
expr_stmt|;
name|mutex
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
name|void
name|notifyServerFailed
parameter_list|()
block|{
synchronized|synchronized
init|(
name|mutex
init|)
block|{
name|serverIsStopped
operator|=
literal|true
expr_stmt|;
name|serverLaunchFailed
operator|=
literal|true
expr_stmt|;
name|mutex
operator|.
name|notifyAll
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getCommand
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|cmd
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|cmd
operator|.
name|add
argument_list|(
name|javaExe
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|properties
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|cmd
operator|.
name|add
argument_list|(
literal|"-D"
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Object
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|TestUtil
operator|.
name|getAllPorts
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|cmd
operator|.
name|add
argument_list|(
literal|"-D"
operator|+
name|entry
operator|.
name|getKey
argument_list|()
operator|+
literal|"="
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"java.awt.headless"
argument_list|)
condition|)
block|{
name|cmd
operator|.
name|add
argument_list|(
literal|"-Djava.awt.headless=true"
argument_list|)
expr_stmt|;
block|}
name|String
name|vmargs
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"server.launcher.vmargs"
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|vmargs
argument_list|)
condition|)
block|{
name|cmd
operator|.
name|add
argument_list|(
literal|"-ea"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|vmargs
operator|=
name|vmargs
operator|.
name|trim
argument_list|()
expr_stmt|;
name|int
name|idx
init|=
name|vmargs
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
decl_stmt|;
while|while
condition|(
name|idx
operator|!=
operator|-
literal|1
condition|)
block|{
name|cmd
operator|.
name|add
argument_list|(
name|vmargs
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|idx
argument_list|)
argument_list|)
expr_stmt|;
name|vmargs
operator|=
name|vmargs
operator|.
name|substring
argument_list|(
name|idx
operator|+
literal|1
argument_list|)
expr_stmt|;
name|idx
operator|=
name|vmargs
operator|.
name|indexOf
argument_list|(
literal|' '
argument_list|)
expr_stmt|;
block|}
name|cmd
operator|.
name|add
argument_list|(
name|vmargs
argument_list|)
expr_stmt|;
block|}
name|String
name|portClose
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.transports.http_jetty.DontClosePort"
argument_list|)
decl_stmt|;
if|if
condition|(
name|portClose
operator|!=
literal|null
condition|)
block|{
name|cmd
operator|.
name|add
argument_list|(
literal|"-Dorg.apache.cxf.transports.http_jetty.DontClosePort="
operator|+
name|portClose
argument_list|)
expr_stmt|;
block|}
name|String
name|loggingPropertiesFile
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.util.logging.config.file"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|loggingPropertiesFile
condition|)
block|{
name|cmd
operator|.
name|add
argument_list|(
literal|"-Djava.util.logging.config.file="
operator|+
name|loggingPropertiesFile
argument_list|)
expr_stmt|;
block|}
name|cmd
operator|.
name|add
argument_list|(
literal|"-classpath"
argument_list|)
expr_stmt|;
name|ClassLoader
name|loader
init|=
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|StringBuffer
name|classpath
init|=
operator|new
name|StringBuffer
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.class.path"
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|classpath
operator|.
name|indexOf
argument_list|(
literal|"/.compatibility/"
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|classpath
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
comment|//on OSX, the compatibility lib brclasspath.indexOf("/.compatibility/")
name|int
name|idx
init|=
name|classpath
operator|.
name|indexOf
argument_list|(
literal|"/.compatibility/"
argument_list|)
decl_stmt|;
name|int
name|idx1
init|=
name|classpath
operator|.
name|lastIndexOf
argument_list|(
literal|":"
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|int
name|idx2
init|=
name|classpath
operator|.
name|indexOf
argument_list|(
literal|":"
argument_list|,
name|idx
argument_list|)
decl_stmt|;
name|classpath
operator|.
name|replace
argument_list|(
name|idx1
argument_list|,
name|idx2
argument_list|,
literal|":"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|loader
operator|instanceof
name|URLClassLoader
condition|)
block|{
name|URLClassLoader
name|urlloader
init|=
operator|(
name|URLClassLoader
operator|)
name|loader
decl_stmt|;
for|for
control|(
name|URL
name|url
range|:
name|urlloader
operator|.
name|getURLs
argument_list|()
control|)
block|{
name|classpath
operator|.
name|append
argument_list|(
name|File
operator|.
name|pathSeparatorChar
argument_list|)
expr_stmt|;
name|classpath
operator|.
name|append
argument_list|(
name|url
operator|.
name|toURI
argument_list|()
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|cmd
operator|.
name|add
argument_list|(
name|classpath
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// If the client set the transformer factory property,
comment|// we want the server to also set that property.
name|String
name|transformerProperty
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"javax.xml.transform.TransformerFactory"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|transformerProperty
condition|)
block|{
name|cmd
operator|.
name|add
argument_list|(
literal|"-Djavax.xml.transform.TransformerFactory="
operator|+
name|transformerProperty
argument_list|)
expr_stmt|;
block|}
name|String
name|validationMode
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"spring.validation.mode"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|validationMode
condition|)
block|{
name|cmd
operator|.
name|add
argument_list|(
literal|"-Dspring.validation.mode="
operator|+
name|validationMode
argument_list|)
expr_stmt|;
block|}
name|String
name|derbyHome
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"derby.system.home"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|derbyHome
condition|)
block|{
name|cmd
operator|.
name|add
argument_list|(
literal|"-Dderby.system.home="
operator|+
name|derbyHome
argument_list|)
expr_stmt|;
block|}
name|String
name|tmp
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"java.io.tmpdir"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|tmp
condition|)
block|{
name|cmd
operator|.
name|add
argument_list|(
literal|"-Djava.io.tmpdir="
operator|+
name|tmp
argument_list|)
expr_stmt|;
block|}
name|cmd
operator|.
name|add
argument_list|(
name|className
argument_list|)
expr_stmt|;
if|if
condition|(
literal|null
operator|!=
name|serverArgs
condition|)
block|{
for|for
control|(
name|String
name|s
range|:
name|serverArgs
control|)
block|{
name|cmd
operator|.
name|add
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cmd
return|;
block|}
specifier|static
class|class
name|Mutex
block|{
comment|// empty
block|}
specifier|static
class|class
name|TimeoutCounter
block|{
specifier|private
specifier|final
name|long
name|expectedEndTime
decl_stmt|;
specifier|public
name|TimeoutCounter
parameter_list|(
name|long
name|theExpectedTimeout
parameter_list|)
block|{
name|expectedEndTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|+
name|theExpectedTimeout
expr_stmt|;
block|}
specifier|public
name|boolean
name|isTimeoutExpired
parameter_list|()
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|>
name|expectedEndTime
return|;
block|}
block|}
block|}
end_class

end_unit

