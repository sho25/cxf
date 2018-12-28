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
name|tools
operator|.
name|common
operator|.
name|model
package|;
end_package

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_class
specifier|public
class|class
name|JavaClassTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetterSetter
parameter_list|()
throws|throws
name|Exception
block|{
name|JavaField
name|field
init|=
operator|new
name|JavaField
argument_list|(
literal|"arg0"
argument_list|,
literal|"org.apache.cxf.tools.fortest.withannotation.doc.TestDataBean"
argument_list|,
literal|"http://doc.withannotation.fortest.tools.cxf.apache.org/"
argument_list|)
decl_stmt|;
name|JavaClass
name|clz
init|=
operator|new
name|JavaClass
argument_list|()
decl_stmt|;
name|clz
operator|.
name|setFullClassName
argument_list|(
literal|"org.apache.cxf.tools.fortest.withannotation.doc.jaxws.EchoDataBean"
argument_list|)
expr_stmt|;
name|JavaMethod
name|getter
init|=
name|clz
operator|.
name|appendGetter
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getArg0"
argument_list|,
name|getter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf.tools.fortest.withannotation.doc.TestDataBean"
argument_list|,
name|getter
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arg0"
argument_list|,
name|getter
operator|.
name|getReturn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|JavaMethod
name|setter
init|=
name|clz
operator|.
name|appendSetter
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"setArg0"
argument_list|,
name|setter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"void"
argument_list|,
name|setter
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"arg0"
argument_list|,
name|getter
operator|.
name|getReturn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.cxf.tools.fortest.withannotation.doc.TestDataBean"
argument_list|,
name|setter
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetterSetterStringArray
parameter_list|()
block|{
name|JavaField
name|field
init|=
operator|new
name|JavaField
argument_list|(
literal|"array"
argument_list|,
literal|"String[]"
argument_list|,
literal|"http://doc.withannotation.fortest.tools.cxf.apache.org/"
argument_list|)
decl_stmt|;
name|JavaClass
name|clz
init|=
operator|new
name|JavaClass
argument_list|()
decl_stmt|;
name|clz
operator|.
name|setFullClassName
argument_list|(
literal|"org.apache.cxf.tools.fortest.withannotation.doc.jaxws.SayHi"
argument_list|)
expr_stmt|;
name|JavaMethod
name|getter
init|=
name|clz
operator|.
name|appendGetter
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"getArray"
argument_list|,
name|getter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String[]"
argument_list|,
name|getter
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"array"
argument_list|,
name|getter
operator|.
name|getReturn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"return this.array;"
argument_list|,
name|getter
operator|.
name|getJavaCodeBlock
argument_list|()
operator|.
name|getExpressions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|JavaMethod
name|setter
init|=
name|clz
operator|.
name|appendSetter
argument_list|(
name|field
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"setArray"
argument_list|,
name|setter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"void"
argument_list|,
name|setter
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"array"
argument_list|,
name|getter
operator|.
name|getReturn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String[]"
argument_list|,
name|setter
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"this.array = newArray;"
argument_list|,
name|setter
operator|.
name|getJavaCodeBlock
argument_list|()
operator|.
name|getExpressions
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|field
operator|=
operator|new
name|JavaField
argument_list|(
literal|"return"
argument_list|,
literal|"String[]"
argument_list|,
literal|"http://doc.withannotation.fortest.tools.cxf.apache.org/"
argument_list|)
expr_stmt|;
name|clz
operator|=
operator|new
name|JavaClass
argument_list|()
expr_stmt|;
name|clz
operator|.
name|setFullClassName
argument_list|(
literal|"org.apache.cxf.tools.fortest.withannotation.doc.jaxws.SayHiResponse"
argument_list|)
expr_stmt|;
name|getter
operator|=
name|clz
operator|.
name|appendGetter
argument_list|(
name|field
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"getReturn"
argument_list|,
name|getter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String[]"
argument_list|,
name|getter
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"_return"
argument_list|,
name|getter
operator|.
name|getReturn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|setter
operator|=
name|clz
operator|.
name|appendSetter
argument_list|(
name|field
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"setReturn"
argument_list|,
name|setter
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"void"
argument_list|,
name|setter
operator|.
name|getReturn
argument_list|()
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"_return"
argument_list|,
name|getter
operator|.
name|getReturn
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"String[]"
argument_list|,
name|setter
operator|.
name|getParameters
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

