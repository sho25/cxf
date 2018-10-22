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
name|pat
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
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
name|BusFactory
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|TestCaseBase
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
name|boolean
name|initialized
decl_stmt|;
specifier|protected
name|String
name|wsdlPath
decl_stmt|;
specifier|protected
name|String
name|serviceName
decl_stmt|;
specifier|protected
name|String
name|portName
decl_stmt|;
specifier|protected
name|String
name|operationName
decl_stmt|;
specifier|protected
name|String
name|hostname
decl_stmt|;
specifier|protected
name|String
name|hostport
decl_stmt|;
specifier|protected
name|int
name|packetSize
init|=
literal|1
decl_stmt|;
specifier|protected
name|boolean
name|usingTime
decl_stmt|;
specifier|protected
name|int
name|amount
init|=
literal|1
decl_stmt|;
specifier|protected
name|String
name|wsdlNameSpace
decl_stmt|;
specifier|protected
name|List
argument_list|<
name|TestResult
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
specifier|protected
name|int
name|numberOfThreads
decl_stmt|;
specifier|protected
name|String
name|busCfg
decl_stmt|;
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
index|[]
name|args
decl_stmt|;
specifier|private
name|String
name|faultReason
init|=
literal|"no error"
decl_stmt|;
specifier|private
name|boolean
name|timedTestDone
init|=
literal|false
decl_stmt|;
specifier|private
name|boolean
name|doWarmup
init|=
literal|true
decl_stmt|;
specifier|public
name|TestCaseBase
parameter_list|()
block|{
name|this
argument_list|(
literal|"DEFAULT TESTCASE"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TestCaseBase
parameter_list|(
name|String
name|cname
parameter_list|)
block|{
name|this
argument_list|(
name|cname
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TestCaseBase
parameter_list|(
name|String
name|cname
parameter_list|,
name|String
index|[]
name|arg
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|cname
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|arg
expr_stmt|;
block|}
specifier|public
name|TestCaseBase
parameter_list|(
name|String
name|cname
parameter_list|,
name|String
index|[]
name|arg
parameter_list|,
name|boolean
name|w
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|cname
expr_stmt|;
name|this
operator|.
name|args
operator|=
name|arg
expr_stmt|;
name|this
operator|.
name|doWarmup
operator|=
name|w
expr_stmt|;
block|}
specifier|public
specifier|abstract
name|void
name|initTestData
parameter_list|()
function_decl|;
specifier|public
name|void
name|init
parameter_list|()
throws|throws
name|Exception
block|{
name|initBus
argument_list|()
expr_stmt|;
name|initTestData
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|processArgs
parameter_list|()
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
name|int
name|argc
init|=
name|args
operator|.
name|length
decl_stmt|;
while|while
condition|(
name|count
operator|<
name|argc
condition|)
block|{
if|if
condition|(
literal|"-WSDL"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
name|wsdlPath
operator|=
name|args
index|[
name|count
operator|+
literal|1
index|]
expr_stmt|;
name|count
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-Service"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
name|serviceName
operator|=
name|args
index|[
name|count
operator|+
literal|1
index|]
expr_stmt|;
name|count
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-Port"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
name|portName
operator|=
name|args
index|[
name|count
operator|+
literal|1
index|]
expr_stmt|;
name|count
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-Operation"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
name|operationName
operator|=
name|args
index|[
name|count
operator|+
literal|1
index|]
expr_stmt|;
name|count
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-BasedOn"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
if|if
condition|(
literal|"Time"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
operator|+
literal|1
index|]
argument_list|)
condition|)
block|{
name|usingTime
operator|=
literal|true
expr_stmt|;
block|}
name|count
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-Amount"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
name|amount
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|count
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
name|count
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-Threads"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
name|numberOfThreads
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|count
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
name|count
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-HostName"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
name|hostname
operator|=
name|args
index|[
name|count
operator|+
literal|1
index|]
expr_stmt|;
name|count
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-HostPort"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
name|hostport
operator|=
name|args
index|[
name|count
operator|+
literal|1
index|]
expr_stmt|;
name|count
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-PacketSize"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
name|packetSize
operator|=
name|Integer
operator|.
name|parseInt
argument_list|(
name|args
index|[
name|count
operator|+
literal|1
index|]
argument_list|)
expr_stmt|;
name|count
operator|+=
literal|2
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"-BUScfg"
operator|.
name|equals
argument_list|(
name|args
index|[
name|count
index|]
argument_list|)
condition|)
block|{
name|busCfg
operator|=
name|args
index|[
name|count
operator|+
literal|1
index|]
expr_stmt|;
name|count
operator|+=
literal|2
expr_stmt|;
block|}
else|else
block|{
name|count
operator|++
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|boolean
name|validate
parameter_list|()
block|{
if|if
condition|(
name|wsdlNameSpace
operator|==
literal|null
operator|||
name|wsdlNameSpace
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"WSDL name space is not specified"
argument_list|)
expr_stmt|;
name|faultReason
operator|=
literal|"Missing WSDL name space"
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|serviceName
operator|==
literal|null
operator|||
name|serviceName
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Service name is not specified"
argument_list|)
expr_stmt|;
name|faultReason
operator|=
literal|"Missing Service name"
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|portName
operator|==
literal|null
operator|||
name|portName
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Port name is not specified"
argument_list|)
expr_stmt|;
name|faultReason
operator|=
literal|"Missing Port name"
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|wsdlPath
operator|==
literal|null
operator|||
name|wsdlPath
operator|.
name|trim
argument_list|()
operator|.
name|equals
argument_list|(
literal|""
argument_list|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"WSDL path is not specifed"
argument_list|)
expr_stmt|;
name|faultReason
operator|=
literal|"Missing WSDL path"
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|// for the cxf init , here do nothing
specifier|public
name|void
name|initBus
parameter_list|()
block|{
if|if
condition|(
name|busCfg
operator|==
literal|null
operator|||
literal|"none"
operator|.
name|equals
argument_list|(
name|busCfg
argument_list|)
condition|)
block|{
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|busCfg
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
block|{     }
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|clearTestResults
argument_list|()
expr_stmt|;
name|printTitle
argument_list|()
expr_stmt|;
name|printSetting
argument_list|(
literal|"Default Setting: "
argument_list|)
expr_stmt|;
name|processArgs
argument_list|()
expr_stmt|;
if|if
condition|(
operator|!
name|validate
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Configure Exception!"
operator|+
name|faultReason
argument_list|)
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
name|init
argument_list|()
expr_stmt|;
name|printSetting
argument_list|(
literal|"Runtime Setting: "
argument_list|)
expr_stmt|;
block|}
name|int
name|initDone
init|=
literal|0
decl_stmt|;
specifier|public
name|void
name|initialize
parameter_list|()
block|{
try|try
block|{
if|if
condition|(
operator|!
name|initialized
condition|)
block|{
name|setUp
argument_list|()
expr_stmt|;
block|}
name|initialized
operator|=
literal|true
expr_stmt|;
if|if
condition|(
operator|!
name|doWarmup
condition|)
block|{
return|return;
block|}
specifier|final
name|int
name|threadCount
init|=
literal|4
decl_stmt|;
specifier|final
name|long
name|timeLimit
init|=
literal|30
decl_stmt|;
specifier|final
name|int
name|countLimit
init|=
literal|1200
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"TestCase "
operator|+
name|name
operator|+
literal|" is warming up the jit. ("
operator|+
name|timeLimit
operator|+
literal|" sec/"
operator|+
name|countLimit
operator|+
literal|" iterations, "
operator|+
name|threadCount
operator|+
literal|" threads)"
argument_list|)
expr_stmt|;
specifier|final
name|long
name|startTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
specifier|final
name|long
name|endTime
init|=
name|startTime
operator|+
operator|(
name|timeLimit
operator|*
literal|1000l
operator|)
decl_stmt|;
specifier|final
name|T
name|t
init|=
name|getPort
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
operator|(
name|threadCount
operator|-
literal|1
operator|)
condition|;
name|x
operator|++
control|)
block|{
operator|new
name|Thread
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|count
operator|<
name|countLimit
operator|||
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|<
name|endTime
condition|)
block|{
name|count
operator|++
expr_stmt|;
name|doJob
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
comment|//System.out.println(count);
comment|//System.out.println("" + (System.currentTimeMillis() - startTime));
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
block|}
operator|++
name|initDone
expr_stmt|;
block|}
block|}
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|int
name|count
init|=
literal|0
decl_stmt|;
while|while
condition|(
name|count
operator|<
name|countLimit
operator|||
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|<
name|endTime
condition|)
block|{
name|count
operator|++
expr_stmt|;
name|doJob
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
comment|//System.out.println(count);
comment|//System.out.println("" + (System.currentTimeMillis() - startTime));
operator|++
name|initDone
expr_stmt|;
while|while
condition|(
name|initDone
operator|!=
name|threadCount
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|100
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
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
specifier|public
specifier|abstract
name|void
name|doJob
parameter_list|(
name|T
name|t
parameter_list|)
function_decl|;
specifier|public
specifier|abstract
name|T
name|getPort
parameter_list|()
function_decl|;
specifier|protected
name|void
name|internalTestRun
parameter_list|(
name|String
name|caseName
parameter_list|,
name|T
name|t
parameter_list|)
throws|throws
name|Exception
block|{
name|int
name|numberOfInvocations
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
literal|25
condition|;
name|x
operator|++
control|)
block|{
comment|//warmup
name|doJob
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
name|long
name|startTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
if|if
condition|(
name|usingTime
condition|)
block|{
while|while
condition|(
operator|!
name|timedTestDone
condition|)
block|{
name|doJob
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|numberOfInvocations
operator|++
expr_stmt|;
block|}
block|}
else|else
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|amount
condition|;
name|i
operator|++
control|)
block|{
name|doJob
argument_list|(
name|t
argument_list|)
expr_stmt|;
name|numberOfInvocations
operator|++
expr_stmt|;
block|}
block|}
name|long
name|endTime
init|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|x
init|=
literal|0
init|;
name|x
operator|<
literal|25
condition|;
name|x
operator|++
control|)
block|{
comment|//keep running so other threads get accurate results
name|doJob
argument_list|(
name|t
argument_list|)
expr_stmt|;
block|}
name|TestResult
name|testResult
init|=
operator|new
name|TestResult
argument_list|(
name|caseName
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|testResult
operator|.
name|compute
argument_list|(
name|startTime
argument_list|,
name|endTime
argument_list|,
name|numberOfInvocations
argument_list|)
expr_stmt|;
name|addTestResult
argument_list|(
name|testResult
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRun
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|numberOfThreads
operator|==
literal|0
condition|)
block|{
name|numberOfThreads
operator|=
literal|1
expr_stmt|;
block|}
name|List
argument_list|<
name|Thread
argument_list|>
name|threadList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|numberOfThreads
condition|;
name|i
operator|++
control|)
block|{
name|TestRunner
argument_list|<
name|T
argument_list|>
name|runner
init|=
operator|new
name|TestRunner
argument_list|<>
argument_list|(
literal|"No."
operator|+
name|i
operator|+
literal|" TestRunner"
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|Thread
name|thread
init|=
operator|new
name|Thread
argument_list|(
name|runner
argument_list|,
literal|"RunnerThread No."
operator|+
name|i
argument_list|)
decl_stmt|;
name|thread
operator|.
name|start
argument_list|()
expr_stmt|;
name|threadList
operator|.
name|add
argument_list|(
name|thread
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|usingTime
condition|)
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|amount
operator|*
literal|1000
argument_list|)
expr_stmt|;
name|timedTestDone
operator|=
literal|true
expr_stmt|;
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|threadList
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Thread
name|thread
init|=
operator|(
name|Thread
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|thread
operator|.
name|join
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
block|}
name|printResult
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|printResult
parameter_list|()
block|{
name|double
name|totalDuration
init|=
literal|0
decl_stmt|;
name|double
name|totalInvocations
init|=
literal|0
decl_stmt|;
for|for
control|(
name|TestResult
name|testResult
range|:
name|results
control|)
block|{
name|totalDuration
operator|=
name|totalDuration
operator|+
name|testResult
operator|.
name|getDuration
argument_list|()
expr_stmt|;
name|totalInvocations
operator|=
name|totalInvocations
operator|+
name|testResult
operator|.
name|getNumOfInvocations
argument_list|()
expr_stmt|;
block|}
name|double
name|totalThroughput
init|=
name|totalInvocations
operator|/
name|totalDuration
decl_stmt|;
name|double
name|totalAvgResponseTime
init|=
name|totalDuration
operator|/
name|totalInvocations
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"=============Overall Test Result============"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Overall Throughput: "
operator|+
name|this
operator|.
name|getOperationName
argument_list|()
operator|+
literal|" "
operator|+
name|totalThroughput
operator|+
name|TestResult
operator|.
name|THROUGHPUT_UNIT
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Overall AVG. response time: "
operator|+
name|totalAvgResponseTime
operator|*
literal|1000
operator|+
name|TestResult
operator|.
name|AVG_UNIT
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|totalInvocations
operator|+
literal|" (invocations), running "
operator|+
name|totalDuration
operator|+
literal|" (sec) "
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"============================================"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|run
parameter_list|()
block|{
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"TestCase "
operator|+
name|name
operator|+
literal|" is running"
argument_list|)
expr_stmt|;
name|testRun
argument_list|()
expr_stmt|;
name|tearDown
argument_list|()
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"TestCase "
operator|+
name|name
operator|+
literal|" is finished"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
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
specifier|protected
name|void
name|clearTestResults
parameter_list|()
block|{
name|results
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|addTestResult
parameter_list|(
name|TestResult
name|result
parameter_list|)
block|{
name|results
operator|.
name|add
argument_list|(
name|result
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|getTestResults
parameter_list|()
block|{
return|return
name|results
return|;
block|}
specifier|public
specifier|abstract
name|void
name|printUsage
parameter_list|()
function_decl|;
specifier|public
name|void
name|printSetting
parameter_list|(
name|String
name|settingType
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|settingType
operator|+
literal|"  [Service] --> "
operator|+
name|serviceName
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|settingType
operator|+
literal|"  [Port] --> "
operator|+
name|portName
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|settingType
operator|+
literal|"  [Operation] --> "
operator|+
name|operationName
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|settingType
operator|+
literal|"  [Threads] --> "
operator|+
name|numberOfThreads
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|settingType
operator|+
literal|"  [Packet Size] --> "
operator|+
name|packetSize
operator|+
literal|" packet(s) "
argument_list|)
expr_stmt|;
if|if
condition|(
name|usingTime
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|settingType
operator|+
literal|"  [Running] -->  "
operator|+
name|amount
operator|+
literal|" (secs)"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|settingType
operator|+
literal|"  [Running] -->  "
operator|+
name|amount
operator|+
literal|" (invocations)"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|printTitle
parameter_list|()
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" ---------------------------------"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|name
operator|+
literal|"  Client (JAVA Version)"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|" ---------------------------------"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setWSDLNameSpace
parameter_list|(
name|String
name|nameSpace
parameter_list|)
block|{
name|this
operator|.
name|wsdlNameSpace
operator|=
name|nameSpace
expr_stmt|;
block|}
specifier|public
name|void
name|setWSDLPath
parameter_list|(
name|String
name|wpath
parameter_list|)
block|{
name|this
operator|.
name|wsdlPath
operator|=
name|wpath
expr_stmt|;
block|}
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|sname
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|sname
expr_stmt|;
block|}
specifier|public
name|void
name|setPortName
parameter_list|(
name|String
name|pname
parameter_list|)
block|{
name|this
operator|.
name|portName
operator|=
name|pname
expr_stmt|;
block|}
specifier|public
name|void
name|setOperationName
parameter_list|(
name|String
name|oname
parameter_list|)
block|{
name|this
operator|.
name|operationName
operator|=
name|oname
expr_stmt|;
block|}
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|this
operator|.
name|serviceName
return|;
block|}
specifier|public
name|String
name|getPortName
parameter_list|()
block|{
return|return
name|this
operator|.
name|portName
return|;
block|}
specifier|public
name|String
name|getOperationName
parameter_list|()
block|{
return|return
name|this
operator|.
name|operationName
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
block|}
end_class

end_unit

