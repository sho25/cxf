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
name|util
operator|.
name|Collection
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
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|AegisServiceImpl
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/*  * We end up here with a part with isElement == true, a non-array element,  * but a complex type for an array of the element.  */
end_comment

begin_class
specifier|public
class|class
name|AegisTest
extends|extends
name|JavascriptRhinoTest
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AegisTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|AegisServiceImpl
name|implementor
decl_stmt|;
specifier|public
name|AegisTest
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
literal|"classpath:AegisBeans.xml"
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
literal|"aegis-service"
argument_list|,
literal|"/org/apache/cxf/javascript/AegisTests.js"
argument_list|,
name|SchemaValidationType
operator|.
name|BOTH
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|(
name|AegisServiceImpl
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
name|acceptAny
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call acceptAny with Raw XML"
operator|+
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|implementor
operator|.
name|prepareToWaitForOneWay
argument_list|()
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCall
argument_list|(
literal|"testAnyNToServerRaw"
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|getAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|implementor
operator|.
name|waitForOneWay
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"before items"
argument_list|,
name|implementor
operator|.
name|getAcceptedString
argument_list|()
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|Document
argument_list|>
name|something
init|=
name|implementor
operator|.
name|getAcceptedCollection
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|something
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
name|callAcceptAny
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
return|return
name|acceptAny
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
name|acceptAnyTyped
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call acceptAny with Raw XML and xsi:type"
operator|+
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|implementor
operator|.
name|prepareToWaitForOneWay
argument_list|()
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCall
argument_list|(
literal|"testAnyNToServerRawTyped"
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|getAddress
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|implementor
operator|.
name|waitForOneWay
argument_list|()
expr_stmt|;
name|Collection
argument_list|<
name|Object
argument_list|>
name|something
init|=
name|implementor
operator|.
name|getAcceptedObjects
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|something
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
name|callAcceptAnyTyped
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
return|return
name|acceptAnyTyped
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
name|returnBeanWithAnyTypeArray
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
literal|"testReturningBeanWithAnyTypeArray"
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
comment|//This method returns a 'BeanWithAnyTypeArray'.
comment|//start by looking at the string.
name|String
name|beanString
init|=
operator|(
name|String
operator|)
name|testUtilities
operator|.
name|rhinoEvaluate
argument_list|(
literal|"globalResponseObject._return._string"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"lima"
argument_list|,
name|beanString
argument_list|)
expr_stmt|;
name|Object
name|o1
init|=
name|testUtilities
operator|.
name|rhinoEvaluate
argument_list|(
literal|"globalResponseObject._return._objects._anyType[0]"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|o1
argument_list|)
expr_stmt|;
name|String
name|marker
init|=
name|testUtilities
operator|.
name|rhinoEvaluateConvert
argument_list|(
literal|"globalResponseObject._return._objects._anyType[0].typeMarker"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"aegis_fortest_javascript_cxf_apache_org_Mammal"
argument_list|,
name|marker
argument_list|)
expr_stmt|;
name|Object
name|intValue
init|=
name|testUtilities
operator|.
name|rhinoEvaluate
argument_list|(
literal|"globalResponseObject._return._objects._anyType[1]"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
literal|42
argument_list|)
argument_list|,
operator|new
name|Float
argument_list|(
name|intValue
operator|.
name|toString
argument_list|()
argument_list|)
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
name|callReturnBeanWithAnyTypeArray
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
return|return
name|returnBeanWithAnyTypeArray
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

