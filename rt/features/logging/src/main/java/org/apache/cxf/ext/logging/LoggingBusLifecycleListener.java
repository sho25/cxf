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
name|ext
operator|.
name|logging
package|;
end_package

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
name|buslifecycle
operator|.
name|BusLifeCycleListener
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
name|buslifecycle
operator|.
name|BusLifeCycleManager
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|LoggingBusLifecycleListener
implements|implements
name|BusLifeCycleListener
block|{
specifier|static
specifier|final
name|boolean
name|FORCE_LOGGING
decl_stmt|;
specifier|static
specifier|final
name|boolean
name|FORCE_PRETTY
decl_stmt|;
static|static
block|{
name|boolean
name|b
init|=
literal|false
decl_stmt|;
name|boolean
name|pretty
init|=
literal|false
decl_stmt|;
try|try
block|{
name|String
name|prop
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"org.apache.cxf.logging.enabled"
argument_list|,
literal|"false"
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"pretty"
operator|.
name|equals
argument_list|(
name|prop
argument_list|)
condition|)
block|{
name|b
operator|=
literal|true
expr_stmt|;
name|pretty
operator|=
literal|true
expr_stmt|;
block|}
else|else
block|{
name|b
operator|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|prop
argument_list|)
expr_stmt|;
comment|//treat these all the same
name|b
operator||=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"com.sun.xml.ws.transport.local.LocalTransportPipe.dump"
argument_list|)
expr_stmt|;
name|b
operator||=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"com.sun.xml.ws.util.pipe.StandaloneTubeAssembler.dump"
argument_list|)
expr_stmt|;
name|b
operator||=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump"
argument_list|)
expr_stmt|;
name|b
operator||=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"com.sun.xml.ws.transport.http.HttpAdapter.dump"
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
block|}
name|FORCE_LOGGING
operator|=
name|b
expr_stmt|;
name|FORCE_PRETTY
operator|=
name|pretty
expr_stmt|;
block|}
specifier|private
specifier|final
name|Bus
name|bus
decl_stmt|;
specifier|public
name|LoggingBusLifecycleListener
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
name|bus
operator|.
name|getExtension
argument_list|(
name|BusLifeCycleManager
operator|.
name|class
argument_list|)
operator|.
name|registerLifeCycleListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|initComplete
parameter_list|()
block|{
if|if
condition|(
name|FORCE_LOGGING
condition|)
block|{
name|LoggingFeature
name|feature
init|=
operator|new
name|LoggingFeature
argument_list|()
decl_stmt|;
name|feature
operator|.
name|setPrettyLogging
argument_list|(
name|FORCE_PRETTY
argument_list|)
expr_stmt|;
name|bus
operator|.
name|getFeatures
argument_list|()
operator|.
name|add
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|feature
operator|.
name|initialize
argument_list|(
name|bus
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|preShutdown
parameter_list|()
block|{     }
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|postShutdown
parameter_list|()
block|{     }
block|}
end_class

end_unit

