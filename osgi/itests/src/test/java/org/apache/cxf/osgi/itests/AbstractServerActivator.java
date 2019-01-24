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
name|osgi
operator|.
name|itests
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
name|endpoint
operator|.
name|Server
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleActivator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|BundleContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Filter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|InvalidSyntaxException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|util
operator|.
name|tracker
operator|.
name|ServiceTracker
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractServerActivator
implements|implements
name|BundleActivator
block|{
specifier|private
name|Server
name|server
decl_stmt|;
specifier|public
specifier|static
name|void
name|awaitService
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|,
name|String
name|filter
parameter_list|,
name|long
name|timeout
parameter_list|)
throws|throws
name|InvalidSyntaxException
throws|,
name|InterruptedException
block|{
name|Filter
name|serviceFilter
init|=
name|bundleContext
operator|.
name|createFilter
argument_list|(
name|filter
argument_list|)
decl_stmt|;
name|ServiceTracker
argument_list|<
name|Object
argument_list|,
name|?
argument_list|>
name|tracker
init|=
operator|new
name|ServiceTracker
argument_list|<>
argument_list|(
name|bundleContext
argument_list|,
name|serviceFilter
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|tracker
operator|.
name|open
argument_list|()
expr_stmt|;
name|Object
name|service
init|=
name|tracker
operator|.
name|waitForService
argument_list|(
name|timeout
argument_list|)
decl_stmt|;
name|tracker
operator|.
name|close
argument_list|()
expr_stmt|;
if|if
condition|(
name|service
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Expected service with filter "
operator|+
name|filter
operator|+
literal|" was not found"
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|void
name|awaitCxfServlet
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|InvalidSyntaxException
throws|,
name|InterruptedException
block|{
name|awaitService
argument_list|(
name|bundleContext
argument_list|,
literal|"("
operator|+
name|Constants
operator|.
name|OBJECTCLASS
operator|+
literal|"=javax.servlet.ServletContext)"
argument_list|,
literal|60000L
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|start
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|Exception
block|{
name|awaitCxfServlet
argument_list|(
name|bundleContext
argument_list|)
expr_stmt|;
name|server
operator|=
name|createServer
argument_list|()
expr_stmt|;
block|}
specifier|protected
specifier|abstract
name|Server
name|createServer
parameter_list|()
function_decl|;
annotation|@
name|Override
specifier|public
name|void
name|stop
parameter_list|(
name|BundleContext
name|bundleContext
parameter_list|)
throws|throws
name|Exception
block|{
name|server
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit
