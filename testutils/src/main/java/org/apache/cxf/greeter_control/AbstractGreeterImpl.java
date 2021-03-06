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
name|greeter_control
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Future
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|AsyncHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Response
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
name|greeter_control
operator|.
name|types
operator|.
name|FaultDetail
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
name|greeter_control
operator|.
name|types
operator|.
name|GreetMeResponse
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
name|greeter_control
operator|.
name|types
operator|.
name|PingMeResponse
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
name|greeter_control
operator|.
name|types
operator|.
name|SayHiResponse
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|AbstractGreeterImpl
implements|implements
name|Greeter
block|{
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
name|AbstractGreeterImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|long
name|delay
decl_stmt|;
specifier|private
name|String
name|lastOnewayArg
decl_stmt|;
specifier|private
name|boolean
name|throwAlways
decl_stmt|;
specifier|private
name|boolean
name|useLastOnewayArg
decl_stmt|;
specifier|private
name|int
name|pingMeCount
decl_stmt|;
specifier|public
name|long
name|getDelay
parameter_list|()
block|{
return|return
name|delay
return|;
block|}
specifier|public
name|void
name|setDelay
parameter_list|(
name|long
name|d
parameter_list|)
block|{
name|delay
operator|=
name|d
expr_stmt|;
block|}
specifier|public
name|void
name|resetLastOnewayArg
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|lastOnewayArg
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|useLastOnewayArg
parameter_list|(
name|Boolean
name|use
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|useLastOnewayArg
operator|=
name|use
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setThrowAlways
parameter_list|(
name|boolean
name|t
parameter_list|)
block|{
name|throwAlways
operator|=
name|t
expr_stmt|;
block|}
specifier|public
name|String
name|greetMe
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Executing operation greetMe with parameter: "
operator|+
name|arg0
argument_list|)
expr_stmt|;
if|if
condition|(
name|delay
operator|>
literal|0
condition|)
block|{
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|delay
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
block|}
name|String
name|result
init|=
literal|null
decl_stmt|;
synchronized|synchronized
init|(
name|this
init|)
block|{
name|result
operator|=
name|useLastOnewayArg
condition|?
name|lastOnewayArg
else|:
name|arg0
operator|.
name|toUpperCase
argument_list|()
expr_stmt|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"returning: "
operator|+
name|result
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|greetMeAsync
parameter_list|(
name|String
name|arg0
parameter_list|,
name|AsyncHandler
argument_list|<
name|GreetMeResponse
argument_list|>
name|arg1
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Response
argument_list|<
name|GreetMeResponse
argument_list|>
name|greetMeAsync
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|greetMeOneWay
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
synchronized|synchronized
init|(
name|this
init|)
block|{
name|lastOnewayArg
operator|=
name|arg0
expr_stmt|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Executing operation greetMeOneWay with parameter: "
operator|+
name|arg0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|pingMe
parameter_list|()
throws|throws
name|PingMeFault
block|{
name|pingMeCount
operator|++
expr_stmt|;
if|if
condition|(
operator|(
name|pingMeCount
operator|%
literal|2
operator|)
operator|==
literal|0
operator|||
name|throwAlways
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Throwing PingMeFault while executiong operation pingMe"
argument_list|)
expr_stmt|;
name|FaultDetail
name|fd
init|=
operator|new
name|FaultDetail
argument_list|()
decl_stmt|;
name|fd
operator|.
name|setMajor
argument_list|(
operator|(
name|short
operator|)
literal|2
argument_list|)
expr_stmt|;
name|fd
operator|.
name|setMinor
argument_list|(
operator|(
name|short
operator|)
literal|1
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|PingMeFault
argument_list|(
literal|"Pings succeed only every other time."
argument_list|,
name|fd
argument_list|)
throw|;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Executing operation pingMe"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Response
argument_list|<
name|PingMeResponse
argument_list|>
name|pingMeAsync
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|pingMeAsync
parameter_list|(
name|AsyncHandler
argument_list|<
name|PingMeResponse
argument_list|>
name|arg0
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|String
name|sayHi
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Response
argument_list|<
name|SayHiResponse
argument_list|>
name|sayHiAsync
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Future
argument_list|<
name|?
argument_list|>
name|sayHiAsync
parameter_list|(
name|AsyncHandler
argument_list|<
name|SayHiResponse
argument_list|>
name|arg0
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

