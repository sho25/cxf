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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|annotations
operator|.
name|SchemaValidation
operator|.
name|SchemaValidationType
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
name|helpers
operator|.
name|IOUtils
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
name|apache
operator|.
name|cxf
operator|.
name|javascript
operator|.
name|fortest
operator|.
name|MtoMImpl
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
name|TestUtil
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

begin_comment
comment|/*  * We end up here with a part with isElement == true, a non-array element,  * but a complex type for an array of the element.  */
end_comment

begin_class
specifier|public
class|class
name|MtoMTest
extends|extends
name|JavascriptRhinoTest
block|{
name|MtoMImpl
name|implementor
decl_stmt|;
specifier|public
name|MtoMTest
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
name|void
name|additionalSpringConfiguration
parameter_list|(
name|GenericApplicationContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{     }
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getConfigLocations
parameter_list|()
block|{
name|TestUtil
operator|.
name|getNewPortNumber
argument_list|(
literal|"TestPort"
argument_list|)
expr_stmt|;
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath:MtoMBeans.xml"
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
literal|"mtom-service-endpoint"
argument_list|,
literal|"/org/apache/cxf/javascript/MtoMTests.js"
argument_list|,
name|SchemaValidationType
operator|.
name|NONE
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|(
name|MtoMImpl
operator|)
name|rawImplementor
expr_stmt|;
name|implementor
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Void
name|acceptMtoMString
parameter_list|(
name|Context
name|context
parameter_list|)
throws|throws
name|IOException
block|{
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"testMtoMString"
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
name|assertEquals
argument_list|(
literal|"disorganized<organized"
argument_list|,
name|implementor
operator|.
name|getLastDHBean
argument_list|()
operator|.
name|getOrdinary
argument_list|()
argument_list|)
expr_stmt|;
name|InputStream
name|dis
init|=
name|implementor
operator|.
name|getLastDHBean
argument_list|()
operator|.
name|getNotXml10
argument_list|()
operator|.
name|getInputStream
argument_list|()
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|2048
index|]
decl_stmt|;
name|int
name|byteCount
init|=
name|dis
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
literal|2048
argument_list|)
decl_stmt|;
name|String
name|stuff
init|=
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|byteCount
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"<html>\u0027</html>"
argument_list|,
name|stuff
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|Void
name|sendMtoMString
parameter_list|(
name|Context
name|context
parameter_list|)
throws|throws
name|IOException
block|{
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"testMtoMReply"
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
literal|30
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
name|errorStatus
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|errorText
argument_list|)
expr_stmt|;
name|String
name|unpacked
init|=
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"globalResponseObject._notXml10"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|unpacked
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
name|sendMtoMStringTest
parameter_list|()
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
try|try
block|{
return|return
name|sendMtoMString
argument_list|(
name|context
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|acceptMtoMStringTest
parameter_list|()
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
try|try
block|{
return|return
name|acceptMtoMString
argument_list|(
name|context
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

