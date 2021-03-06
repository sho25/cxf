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
name|jaxb
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

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
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlAccessorType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlType
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
name|databinding
operator|.
name|WrapperHelper
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
name|assertFalse
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|JAXBWrapperHelperTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|getBooleanTypeWrappedPart
parameter_list|()
throws|throws
name|Exception
block|{
name|SetIsOK
name|ok
init|=
operator|new
name|SetIsOK
argument_list|()
decl_stmt|;
name|ok
operator|.
name|setParameter3
argument_list|(
operator|new
name|boolean
index|[]
block|{
literal|true
block|,
literal|false
block|}
argument_list|)
expr_stmt|;
name|ok
operator|.
name|setParameter4
argument_list|(
literal|"hello"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|partNames
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"Parameter1"
block|,
literal|"Parameter2"
block|,
literal|"Parameter3"
block|,
literal|"Parameter4"
block|,
literal|"Parameter5"
block|,         }
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|elTypeNames
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"boolean"
block|,
literal|"int"
block|,
literal|"boolean"
block|,
literal|"string"
block|,
literal|"string"
block|,         }
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|partClasses
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|Boolean
operator|.
name|TYPE
operator|,
name|Integer
operator|.
name|TYPE
operator|,
name|boolean
index|[]
operator|.
expr|class
operator|,
name|String
operator|.
name|class
operator|,
name|List
operator|.
name|class
operator|,
block|}
block|)
function|;
name|WrapperHelper
name|wh
init|=
operator|new
name|JAXBDataBinding
argument_list|()
operator|.
name|createWrapperHelper
argument_list|(
name|SetIsOK
operator|.
name|class
argument_list|,
literal|null
argument_list|,
name|partNames
argument_list|,
name|elTypeNames
argument_list|,
name|partClasses
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|lst
init|=
name|wh
operator|.
name|getWrapperParts
argument_list|(
name|ok
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|lst
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lst
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|Boolean
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lst
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|instanceof
name|Integer
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|lst
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|instanceof
name|boolean
index|[]
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|(
operator|(
name|boolean
index|[]
operator|)
name|lst
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|(
operator|(
name|boolean
index|[]
operator|)
name|lst
operator|.
name|get
argument_list|(
literal|2
argument_list|)
operator|)
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|lst
operator|.
name|get
argument_list|(
literal|3
argument_list|)
argument_list|)
expr_stmt|;
name|lst
operator|.
name|set
argument_list|(
literal|0
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|Object
name|o
init|=
name|wh
operator|.
name|createWrapperObject
argument_list|(
name|lst
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ok
operator|=
operator|(
name|SetIsOK
operator|)
name|o
expr_stmt|;
name|assertTrue
argument_list|(
name|ok
operator|.
name|isParameter1
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|ok
operator|.
name|getParameter3
argument_list|()
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|ok
operator|.
name|getParameter3
argument_list|()
index|[
literal|1
index|]
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello"
argument_list|,
name|ok
operator|.
name|getParameter4
argument_list|()
argument_list|)
expr_stmt|;
block|}
end_class

begin_class
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
annotation|@
name|XmlType
argument_list|(
name|name
operator|=
literal|""
argument_list|,
name|propOrder
operator|=
block|{
literal|"parameter1"
block|,
literal|"parameter2"
block|,
literal|"parameter3"
block|,
literal|"parameter4"
block|}
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"setIsOK"
argument_list|)
specifier|public
specifier|static
class|class
name|SetIsOK
block|{
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"Parameter1"
argument_list|)
specifier|protected
name|boolean
name|parameter1
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"Parameter2"
argument_list|)
specifier|protected
name|int
name|parameter2
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"Parameter3"
argument_list|)
specifier|protected
name|boolean
index|[]
name|parameter3
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"Parameter4"
argument_list|)
specifier|protected
name|String
name|parameter4
decl_stmt|;
annotation|@
name|XmlElement
argument_list|(
name|name
operator|=
literal|"Parameter5"
argument_list|)
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|parameter5
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/**          * Gets the value of the parameter1 property.          *          */
specifier|public
name|boolean
name|isParameter1
parameter_list|()
block|{
return|return
name|parameter1
return|;
block|}
comment|/**          * Sets the value of the parameter1 property.          *          */
specifier|public
name|void
name|setParameter1
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|parameter1
operator|=
name|value
expr_stmt|;
block|}
comment|/**          * Gets the value of the parameter2 property.          *          */
specifier|public
name|int
name|getParameter2
parameter_list|()
block|{
return|return
name|parameter2
return|;
block|}
comment|/**          * Sets the value of the parameter2 property.          *          */
specifier|public
name|void
name|setParameter2
parameter_list|(
name|int
name|value
parameter_list|)
block|{
name|this
operator|.
name|parameter2
operator|=
name|value
expr_stmt|;
block|}
comment|/**          * Gets the value of the parameter2 property.          *          */
specifier|public
name|boolean
index|[]
name|getParameter3
parameter_list|()
block|{
return|return
name|parameter3
return|;
block|}
comment|/**          * Sets the value of the parameter2 property.          *          */
specifier|public
name|void
name|setParameter3
parameter_list|(
name|boolean
index|[]
name|value
parameter_list|)
block|{
name|this
operator|.
name|parameter3
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|String
name|getParameter4
parameter_list|()
block|{
return|return
name|parameter4
return|;
block|}
specifier|public
name|void
name|setParameter4
parameter_list|(
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|parameter4
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getParameter5
parameter_list|()
block|{
return|return
name|parameter5
return|;
block|}
block|}
end_class

unit|}
end_unit

