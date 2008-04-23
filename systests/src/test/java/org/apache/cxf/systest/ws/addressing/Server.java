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
name|systest
operator|.
name|ws
operator|.
name|addressing
package|;
end_package

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
name|logging
operator|.
name|Level
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
name|Endpoint
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
name|Bus
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|Interceptor
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
import|;
end_import

begin_class
specifier|public
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
implements|implements
name|VerificationCache
block|{
specifier|static
specifier|final
name|String
name|ADDRESS
init|=
literal|"http://localhost:9008/SoapContext/SoapPort"
decl_stmt|;
specifier|private
name|String
name|verified
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|SpringBusFactory
name|factory
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|(
literal|"org/apache/cxf/systest/ws/addressing/wsa_interceptors.xml"
argument_list|)
decl_stmt|;
name|BusFactory
operator|.
name|setDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|setBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|addVerifiers
argument_list|()
expr_stmt|;
name|GreeterImpl
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|implementor
operator|.
name|verificationCache
operator|=
name|this
expr_stmt|;
name|Endpoint
operator|.
name|publish
argument_list|(
name|ADDRESS
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|addVerifiers
parameter_list|()
block|{
name|MAPVerifier
name|mapVerifier
init|=
operator|new
name|MAPVerifier
argument_list|()
decl_stmt|;
name|mapVerifier
operator|.
name|verificationCache
operator|=
name|this
expr_stmt|;
name|HeaderVerifier
name|headerVerifier
init|=
operator|new
name|HeaderVerifier
argument_list|()
decl_stmt|;
name|headerVerifier
operator|.
name|verificationCache
operator|=
name|this
expr_stmt|;
name|Interceptor
index|[]
name|interceptors
init|=
block|{
name|mapVerifier
block|,
name|headerVerifier
block|}
decl_stmt|;
name|addInterceptors
argument_list|(
name|getBus
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|addInterceptors
argument_list|(
name|getBus
argument_list|()
operator|.
name|getInFaultInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|addInterceptors
argument_list|(
name|getBus
argument_list|()
operator|.
name|getOutInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
name|addInterceptors
argument_list|(
name|getBus
argument_list|()
operator|.
name|getOutFaultInterceptors
argument_list|()
argument_list|,
name|interceptors
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addInterceptors
parameter_list|(
name|List
argument_list|<
name|Interceptor
argument_list|>
name|chain
parameter_list|,
name|Interceptor
index|[]
name|interceptors
parameter_list|)
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
name|interceptors
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|chain
operator|.
name|add
argument_list|(
name|interceptors
index|[
name|i
index|]
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
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
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
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|put
parameter_list|(
name|String
name|verification
parameter_list|)
block|{
if|if
condition|(
name|verification
operator|!=
literal|null
condition|)
block|{
name|verified
operator|=
name|verified
operator|==
literal|null
condition|?
name|verification
else|:
name|verified
operator|+
literal|"; "
operator|+
name|verification
expr_stmt|;
block|}
block|}
comment|/**      * Used to facilitate assertions on server-side behaviour.      *      * @param log logger to use for diagnostics if assertions fail      * @return true if assertions hold      */
specifier|protected
name|boolean
name|verify
parameter_list|(
name|Logger
name|log
parameter_list|)
block|{
if|if
condition|(
name|verified
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
literal|"MAP/Header verification failed: "
operator|+
name|verified
argument_list|)
expr_stmt|;
name|log
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"MAP/Header verification failed: {0}"
argument_list|,
name|verified
argument_list|)
expr_stmt|;
block|}
return|return
name|verified
operator|==
literal|null
return|;
block|}
block|}
end_class

end_unit

