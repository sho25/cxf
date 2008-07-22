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
name|javascript
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
name|net
operator|.
name|URL
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
name|javascript
operator|.
name|JavascriptTestUtilities
operator|.
name|JSRunnable
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
name|javascript
operator|.
name|JavascriptTestUtilities
operator|.
name|Notifier
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mozilla
operator|.
name|javascript
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|GenericApplicationContext
import|;
end_import

begin_comment
comment|/**  *  Test the same schema used in the samples.  */
end_comment

begin_class
specifier|public
class|class
name|GreeterClientTest
extends|extends
name|JavascriptRhinoTest
block|{
specifier|public
name|GreeterClientTest
parameter_list|()
throws|throws
name|Exception
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getConfigLocations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath:GreeterClientTestBeans.xml"
block|}
return|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|Exception
block|{
name|setupRhino
argument_list|(
literal|"greeter-service-endpoint"
argument_list|,
literal|"/org/apache/cxf/javascript/GreeterTests.js"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Void
name|sayHiCaller
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"sayHiTest"
argument_list|,
name|Notifier
operator|.
name|class
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|getAddress
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|notified
init|=
name|notifier
operator|.
name|waitForJavascript
argument_list|(
literal|1000
operator|*
literal|10
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|notified
argument_list|)
expr_stmt|;
name|Integer
name|errorStatus
init|=
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"globalErrorStatus"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|errorStatus
argument_list|)
expr_stmt|;
name|String
name|errorText
init|=
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"globalErrorStatusText"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|errorText
argument_list|)
expr_stmt|;
comment|// this method returns a String inside of an object, since there's an @WebResponse
name|String
name|responseObject
init|=
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"globalResponseObject.getResponseType()"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bonjour"
argument_list|,
name|responseObject
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCallSayHi
parameter_list|()
throws|throws
name|Exception
block|{
name|testUtilities
operator|.
name|runInsideContext
argument_list|(
name|Void
operator|.
name|class
argument_list|,
operator|new
name|JSRunnable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
specifier|public
name|Void
name|run
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
return|return
name|sayHiCaller
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Void
name|sayHiClosureCaller
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"requestClosureTest"
argument_list|,
name|Notifier
operator|.
name|class
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|getAddress
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|boolean
name|notified
init|=
name|notifier
operator|.
name|waitForJavascript
argument_list|(
literal|1000
operator|*
literal|10
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|notified
argument_list|)
expr_stmt|;
name|Integer
name|errorStatus
init|=
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"globalErrorStatus"
argument_list|,
name|Integer
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|errorStatus
argument_list|)
expr_stmt|;
name|String
name|errorText
init|=
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"globalErrorStatusText"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|errorText
argument_list|)
expr_stmt|;
comment|// this method returns a String inside of an object, since there's an @WebResponse
name|String
name|responseObject
init|=
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"globalResponseObject.getResponseType()"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Bonjour"
argument_list|,
name|responseObject
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|Ignore
annotation|@
name|Test
specifier|public
name|void
name|testRequestClosure
parameter_list|()
throws|throws
name|Exception
block|{
name|testUtilities
operator|.
name|runInsideContext
argument_list|(
name|Void
operator|.
name|class
argument_list|,
operator|new
name|JSRunnable
argument_list|<
name|Void
argument_list|>
argument_list|()
block|{
specifier|public
name|Void
name|run
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
return|return
name|sayHiClosureCaller
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getStaticResourceURL
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|staticFile
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"test.html"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
decl_stmt|;
name|staticFile
operator|=
name|staticFile
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
name|staticFile
operator|=
name|staticFile
operator|.
name|getAbsoluteFile
argument_list|()
expr_stmt|;
name|URL
name|furl
init|=
name|staticFile
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
decl_stmt|;
return|return
name|furl
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|additionalSpringConfiguration
parameter_list|(
name|GenericApplicationContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{     }
block|}
end_class

end_unit

