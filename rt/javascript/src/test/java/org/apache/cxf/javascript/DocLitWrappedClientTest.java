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
name|SimpleDocLitWrappedImpl
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

begin_class
specifier|public
class|class
name|DocLitWrappedClientTest
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
name|DocLitWrappedClientTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|DocLitWrappedClientTest
parameter_list|()
throws|throws
name|Exception
block|{
name|super
argument_list|()
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
literal|"dlw-service-endpoint"
argument_list|,
literal|"/org/apache/cxf/javascript/DocLitWrappedTests.js"
argument_list|,
literal|true
argument_list|)
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
literal|"classpath:DocLitWrappedClientTestBeans.xml"
block|}
return|;
block|}
specifier|private
name|Void
name|beanFunctionCaller
parameter_list|(
name|Context
name|context
parameter_list|,
name|boolean
name|useWrapper
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
name|beanTwoNotRequiredItem
operator|=
operator|new
name|TestBean2
argument_list|(
literal|"bean2"
argument_list|)
expr_stmt|;
if|if
condition|(
name|useWrapper
condition|)
block|{
name|beans
index|[
literal|1
index|]
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
comment|// without a wrapper, it can't be null, so put something in there.
name|beans
index|[
literal|1
index|]
operator|=
operator|new
name|TestBean1
argument_list|()
expr_stmt|;
block|}
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
name|testBean1ToJS
argument_list|(
name|testUtilities
argument_list|,
name|context
argument_list|,
name|beans
index|[
literal|1
index|]
argument_list|)
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
literal|"About to call test4 "
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
literal|"test4"
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
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Boolean
operator|.
name|valueOf
argument_list|(
name|useWrapper
argument_list|)
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
comment|// This method returns void, which translated into a Javascript object with no properties.
comment|// really. Void.
name|Object
name|responseObject
init|=
name|testUtilities
operator|.
name|rhinoEvaluate
argument_list|(
literal|"globalResponseObject"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|responseObject
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Context
operator|.
name|getUndefinedValue
argument_list|()
argument_list|,
name|responseObject
argument_list|)
expr_stmt|;
name|SimpleDocLitWrappedImpl
name|impl
init|=
operator|(
name|SimpleDocLitWrappedImpl
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
name|TestBean1
index|[]
name|beansReturned
init|=
name|impl
operator|.
name|getLastBean1Array
argument_list|()
decl_stmt|;
name|assertArrayEquals
argument_list|(
name|beans
argument_list|,
name|beansReturned
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
literal|"about to call test4/beanFunction"
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
argument_list|,
literal|false
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
name|callFunctionWithBeansWrapped
parameter_list|()
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"about to call test4/beanFunction"
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
argument_list|,
literal|true
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
name|callFunctionWithHeader
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
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call testDummyHeader "
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
literal|"testDummyHeader"
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
name|testUtilities
operator|.
name|javaToJS
argument_list|(
literal|"narcissus"
argument_list|)
argument_list|,
literal|null
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
name|assertNotNull
argument_list|(
name|responseObject
argument_list|)
expr_stmt|;
comment|// by default, for doc/lit/wrapped, we end up with a part object with a slot named
comment|// 'return'.
name|String
name|returnValue
init|=
name|testUtilities
operator|.
name|rhinoCallMethodInContext
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|responseObject
argument_list|,
literal|"getReturn"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"narcissus"
argument_list|,
name|returnValue
argument_list|)
expr_stmt|;
return|return
literal|null
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
name|callIntReturnMethod
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
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call test3/IntFunction"
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
literal|"test3"
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
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|17.0
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
operator|(
name|float
operator|)
literal|111.0
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|142
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1240000
argument_list|)
argument_list|)
argument_list|,
literal|null
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
name|assertNotNull
argument_list|(
name|responseObject
argument_list|)
expr_stmt|;
comment|// by default, for doc/lit/wrapped, we end up with a part object with a slot named
comment|// 'return'.
name|int
name|returnValue
init|=
name|testUtilities
operator|.
name|rhinoCallMethodInContext
argument_list|(
name|Integer
operator|.
name|class
argument_list|,
name|responseObject
argument_list|,
literal|"getReturn"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|42
argument_list|,
name|returnValue
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
comment|// well, null AND void.
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|callMethodWithoutWrappers
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
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call test2 "
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
literal|"test2"
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
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|17.0
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
operator|(
name|float
operator|)
literal|111.0
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|142
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1240000
argument_list|)
argument_list|)
argument_list|,
literal|"This is the cereal shot from gnus"
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
name|assertNotNull
argument_list|(
name|responseObject
argument_list|)
expr_stmt|;
comment|// by default, for doc/lit/wrapped, we end up with a part object with a slot named
comment|// 'return'.
name|String
name|returnString
init|=
name|testUtilities
operator|.
name|rhinoCallMethodInContext
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|responseObject
argument_list|,
literal|"getReturn"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cetaceans"
argument_list|,
name|returnString
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
comment|// well, null AND void.
block|}
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|callMethodWithWrappers
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
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call test1 "
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
literal|"test1"
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
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|7.0
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
operator|(
name|float
operator|)
literal|11.0
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|42
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|240000
argument_list|)
argument_list|)
argument_list|,
literal|"This is the cereal shot from guns"
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
name|assertNotNull
argument_list|(
name|responseObject
argument_list|)
expr_stmt|;
name|String
name|returnString
init|=
name|testUtilities
operator|.
name|rhinoCallMethodInContext
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|responseObject
argument_list|,
literal|"getReturnValue"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"eels"
argument_list|,
name|returnString
argument_list|)
expr_stmt|;
return|return
literal|null
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
name|inheritedProperties
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
name|Notifier
name|notifier
init|=
name|testUtilities
operator|.
name|rhinoCallConvert
argument_list|(
literal|"testInheritance"
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
name|SimpleDocLitWrappedImpl
name|impl
init|=
operator|(
name|SimpleDocLitWrappedImpl
operator|)
name|rawImplementor
decl_stmt|;
name|assertEquals
argument_list|(
literal|"less"
argument_list|,
name|impl
operator|.
name|getLastInheritanceTestDerived
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
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
name|callTest2WithNullString
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
name|LOG
operator|.
name|info
argument_list|(
literal|"About to call test2 with null string "
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
literal|"test2"
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
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Double
operator|.
name|valueOf
argument_list|(
literal|17.0
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Float
operator|.
name|valueOf
argument_list|(
operator|(
name|float
operator|)
literal|111.0
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Integer
operator|.
name|valueOf
argument_list|(
literal|142
argument_list|)
argument_list|)
argument_list|,
name|testUtilities
operator|.
name|javaToJS
argument_list|(
name|Long
operator|.
name|valueOf
argument_list|(
literal|1240000
argument_list|)
argument_list|)
argument_list|,
literal|null
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
name|assertNotNull
argument_list|(
name|responseObject
argument_list|)
expr_stmt|;
comment|// by default, for doc/lit/wrapped, we end up with a part object with a slot named
comment|// 'return'.
name|String
name|returnString
init|=
name|testUtilities
operator|.
name|rhinoCallMethodInContext
argument_list|(
name|String
operator|.
name|class
argument_list|,
name|responseObject
argument_list|,
literal|"getReturn"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"cetaceans"
argument_list|,
name|returnString
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
comment|// well, null AND void.
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

