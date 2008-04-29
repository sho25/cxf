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
name|SimpleDocLitBareImpl
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
name|TestBean1
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
name|TestBean2
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
name|mozilla
operator|.
name|javascript
operator|.
name|Scriptable
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
comment|/*  * We end up here with a part with isElement == true, a non-array element,   * but a complex type for an array of the element.  */
end_comment

begin_class
specifier|public
class|class
name|DocLitBareClientTest
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
name|DocLitBareClientTest
operator|.
name|class
argument_list|)
decl_stmt|;
name|SimpleDocLitBareImpl
name|implementor
decl_stmt|;
specifier|public
name|DocLitBareClientTest
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
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath:DocLitBareClientTestBeans.xml"
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
literal|"dlb-service-endpoint"
argument_list|,
literal|"/org/apache/cxf/javascript/DocLitBareTests.js"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|(
name|SimpleDocLitBareImpl
operator|)
name|rawImplementor
expr_stmt|;
name|implementor
operator|.
name|resetLastValues
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Void
name|beanFunctionCaller
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|TestBean1
name|b1
init|=
operator|new
name|TestBean1
argument_list|()
decl_stmt|;
name|b1
operator|.
name|stringItem
operator|=
literal|"strung"
expr_stmt|;
name|TestBean1
index|[]
name|beans
init|=
operator|new
name|TestBean1
index|[
literal|3
index|]
decl_stmt|;
name|beans
index|[
literal|0
index|]
operator|=
operator|new
name|TestBean1
argument_list|()
expr_stmt|;
name|beans
index|[
literal|0
index|]
operator|.
name|stringItem
operator|=
literal|"zerobean"
expr_stmt|;
name|beans
index|[
literal|0
index|]
operator|.
name|beanTwoNotRequiredItem
operator|=
operator|new
name|TestBean2
argument_list|(
literal|"bean2"
argument_list|)
expr_stmt|;
name|beans
index|[
literal|1
index|]
operator|=
literal|null
expr_stmt|;
name|beans
index|[
literal|2
index|]
operator|=
operator|new
name|TestBean1
argument_list|()
expr_stmt|;
name|beans
index|[
literal|2
index|]
operator|.
name|stringItem
operator|=
literal|"twobean"
expr_stmt|;
name|beans
index|[
literal|2
index|]
operator|.
name|optionalIntArrayItem
operator|=
operator|new
name|int
index|[
literal|2
index|]
expr_stmt|;
name|beans
index|[
literal|2
index|]
operator|.
name|optionalIntArrayItem
index|[
literal|0
index|]
operator|=
literal|4
expr_stmt|;
name|beans
index|[
literal|2
index|]
operator|.
name|optionalIntArrayItem
index|[
literal|1
index|]
operator|=
literal|6
expr_stmt|;
name|Object
index|[]
name|jsBeans
init|=
operator|new
name|Object
index|[
literal|3
index|]
decl_stmt|;
name|jsBeans
index|[
literal|0
index|]
operator|=
name|testBean1ToJS
argument_list|(
name|testUtilities
argument_list|,
name|context
argument_list|,
name|beans
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|jsBeans
index|[
literal|1
index|]
operator|=
literal|null
expr_stmt|;
name|jsBeans
index|[
literal|2
index|]
operator|=
name|testBean1ToJS
argument_list|(
name|testUtilities
argument_list|,
name|context
argument_list|,
name|beans
index|[
literal|2
index|]
argument_list|)
expr_stmt|;
name|Scriptable
name|jsBean1
init|=
name|testBean1ToJS
argument_list|(
name|testUtilities
argument_list|,
name|context
argument_list|,
name|b1
argument_list|)
decl_stmt|;
name|Scriptable
name|jsBeanArray
init|=
name|context
operator|.
name|newArray
argument_list|(
name|testUtilities
operator|.
name|getRhinoScope
argument_list|()
argument_list|,
name|jsBeans
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call beanFunctionTest "
operator|+
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"beanFunctionTest"
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
argument_list|,
name|jsBean1
argument_list|,
name|jsBeanArray
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
comment|// this method returns void.
name|Scriptable
name|responseObject
init|=
operator|(
name|Scriptable
operator|)
name|testUtilities
operator|.
name|rhinoEvaluate
argument_list|(
literal|"globalResponseObject"
argument_list|)
decl_stmt|;
comment|// there is no response, this thing returns 'void'
name|assertNull
argument_list|(
name|responseObject
argument_list|)
expr_stmt|;
name|SimpleDocLitBareImpl
name|impl
init|=
operator|(
name|SimpleDocLitBareImpl
operator|)
name|rawImplementor
decl_stmt|;
name|TestBean1
name|b1returned
init|=
name|impl
operator|.
name|getLastBean1
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|b1
argument_list|,
name|b1returned
argument_list|)
expr_stmt|;
comment|// commented out until
comment|//TestBean1[] beansReturned = impl.getLastBean1Array();
comment|//assertArrayEquals(beans, beansReturned);
return|return
literal|null
return|;
block|}
specifier|private
name|Void
name|compliantCaller
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|TestBean1
name|b1
init|=
operator|new
name|TestBean1
argument_list|()
decl_stmt|;
name|b1
operator|.
name|stringItem
operator|=
literal|"strung"
expr_stmt|;
name|b1
operator|.
name|beanTwoNotRequiredItem
operator|=
operator|new
name|TestBean2
argument_list|(
literal|"bean2"
argument_list|)
expr_stmt|;
name|Scriptable
name|jsBean1
init|=
name|testBean1ToJS
argument_list|(
name|testUtilities
argument_list|,
name|context
argument_list|,
name|b1
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call compliant"
operator|+
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"compliantTest"
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
argument_list|,
name|jsBean1
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
comment|//This method returns a String
name|String
name|response
init|=
operator|(
name|String
operator|)
name|testUtilities
operator|.
name|rhinoEvaluate
argument_list|(
literal|"globalResponseObject"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"strung"
argument_list|,
name|response
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|Void
name|actionMethodCaller
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call actionMethod"
operator|+
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"actionMethodTest"
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
argument_list|,
literal|"wrong"
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
comment|//This method returns a String
name|String
name|response
init|=
operator|(
name|String
operator|)
name|testUtilities
operator|.
name|rhinoEvaluate
argument_list|(
literal|"globalResponseObject"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"wrong"
argument_list|,
name|response
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|Void
name|onewayCaller
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call onewayMethod"
operator|+
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCall
argument_list|(
literal|"actionMethodTest"
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|getAddress
argument_list|()
argument_list|)
argument_list|,
literal|"corrigan"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"corrigan"
argument_list|,
name|implementor
operator|.
name|getLastString
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
specifier|private
name|Void
name|compliantNoArgsCaller
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call compliantNoArgs "
operator|+
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"compliantNoArgsTest"
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
comment|//This method returns a String
name|Scriptable
name|response
init|=
operator|(
name|Scriptable
operator|)
name|testUtilities
operator|.
name|rhinoEvaluate
argument_list|(
literal|"globalResponseObject"
argument_list|)
decl_stmt|;
name|String
name|item
init|=
name|testUtilities
operator|.
name|rhinoCallMethodConvert
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|response
argument_list|,
literal|"getStringItem"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"horsefeathers"
argument_list|,
name|item
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
name|callFunctionWithBeans
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"about to call beanFunctionTest"
argument_list|)
expr_stmt|;
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
name|beanFunctionCaller
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|callCompliant
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"about to call compliant"
argument_list|)
expr_stmt|;
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
name|compliantCaller
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|callActionMethod
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"about to call actionMethod"
argument_list|)
expr_stmt|;
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
name|actionMethodCaller
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|callOneway
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"about to call oneway"
argument_list|)
expr_stmt|;
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
name|onewayCaller
argument_list|(
name|context
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|callCompliantNoArgs
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"about to call compliantNoArg"
argument_list|)
expr_stmt|;
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
name|compliantNoArgsCaller
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
name|portObjectCaller
parameter_list|(
name|Context
name|context
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call portObjectTest "
operator|+
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"portObjectTest"
argument_list|,
name|Notifier
operator|.
name|class
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
comment|//This method returns a String
name|Scriptable
name|response
init|=
operator|(
name|Scriptable
operator|)
name|testUtilities
operator|.
name|rhinoEvaluate
argument_list|(
literal|"globalResponseObject"
argument_list|)
decl_stmt|;
name|String
name|item
init|=
name|testUtilities
operator|.
name|rhinoCallMethodConvert
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|response
argument_list|,
literal|"getStringItem"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"horsefeathers"
argument_list|,
name|item
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
name|callPortObject
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"about to call portObject"
argument_list|)
expr_stmt|;
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
name|portObjectCaller
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
specifier|static
name|Scriptable
name|testBean1ToJS
parameter_list|(
name|JavascriptTestUtilities
name|testUtilities
parameter_list|,
name|Context
name|context
parameter_list|,
name|TestBean1
name|b1
parameter_list|)
block|{
if|if
condition|(
name|b1
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
comment|// black is always in fashion. (Really, we can be called with a null).
block|}
name|Scriptable
name|rv
init|=
name|context
operator|.
name|newObject
argument_list|(
name|testUtilities
operator|.
name|getRhinoScope
argument_list|()
argument_list|,
literal|"org_apache_cxf_javascript_testns_testBean1"
argument_list|)
decl_stmt|;
name|testUtilities
operator|.
name|rhinoCallMethod
argument_list|(
name|rv
argument_list|,
literal|"setStringItem"
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|b1
operator|.
name|stringItem
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallMethod
argument_list|(
name|rv
argument_list|,
literal|"setIntItem"
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|b1
operator|.
name|intItem
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallMethod
argument_list|(
name|rv
argument_list|,
literal|"setLongItem"
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|b1
operator|.
name|longItem
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallMethod
argument_list|(
name|rv
argument_list|,
literal|"setBase64Item"
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|b1
operator|.
name|base64Item
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallMethod
argument_list|(
name|rv
argument_list|,
literal|"setOptionalIntItem"
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|b1
operator|.
name|optionalIntItem
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallMethod
argument_list|(
name|rv
argument_list|,
literal|"setOptionalIntArrayItem"
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|b1
operator|.
name|optionalIntArrayItem
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallMethod
argument_list|(
name|rv
argument_list|,
literal|"setDoubleItem"
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|b1
operator|.
name|doubleItem
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallMethod
argument_list|(
name|rv
argument_list|,
literal|"setBeanTwoItem"
argument_list|,
name|testBean2ToJS
argument_list|(
name|testUtilities
argument_list|,
name|context
argument_list|,
name|b1
operator|.
name|beanTwoItem
argument_list|)
argument_list|)
expr_stmt|;
name|testUtilities
operator|.
name|rhinoCallMethod
argument_list|(
name|rv
argument_list|,
literal|"setBeanTwoNotRequiredItem"
argument_list|,
name|testBean2ToJS
argument_list|(
name|testUtilities
argument_list|,
name|context
argument_list|,
name|b1
operator|.
name|beanTwoNotRequiredItem
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|rv
return|;
block|}
specifier|public
specifier|static
name|Object
name|testBean2ToJS
parameter_list|(
name|JavascriptTestUtilities
name|testUtilities
parameter_list|,
name|Context
name|context
parameter_list|,
name|TestBean2
name|beanTwoItem
parameter_list|)
block|{
if|if
condition|(
name|beanTwoItem
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Scriptable
name|rv
init|=
name|context
operator|.
name|newObject
argument_list|(
name|testUtilities
operator|.
name|getRhinoScope
argument_list|()
argument_list|,
literal|"org_apache_cxf_javascript_testns3_testBean2"
argument_list|)
decl_stmt|;
name|testUtilities
operator|.
name|rhinoCallMethod
argument_list|(
name|rv
argument_list|,
literal|"setStringItem"
argument_list|,
name|beanTwoItem
operator|.
name|stringItem
argument_list|)
expr_stmt|;
return|return
name|rv
return|;
block|}
block|}
end_class

end_unit

