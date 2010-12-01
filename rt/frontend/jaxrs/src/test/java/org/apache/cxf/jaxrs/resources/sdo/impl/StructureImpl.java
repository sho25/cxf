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
name|jaxrs
operator|.
name|resources
operator|.
name|sdo
operator|.
name|impl
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
name|List
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
name|jaxrs
operator|.
name|resources
operator|.
name|sdo
operator|.
name|SdoFactory
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
name|jaxrs
operator|.
name|resources
operator|.
name|sdo
operator|.
name|Structure
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|tuscany
operator|.
name|sdo
operator|.
name|impl
operator|.
name|DataObjectBase
import|;
end_import

begin_import
import|import
name|commonj
operator|.
name|sdo
operator|.
name|Type
import|;
end_import

begin_comment
comment|//CHECKSTYLE:OFF
end_comment

begin_class
specifier|public
class|class
name|StructureImpl
extends|extends
name|DataObjectBase
implements|implements
name|Structure
block|{
specifier|public
specifier|static
specifier|final
name|int
name|TEXT
init|=
literal|0
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|INT
init|=
literal|1
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|DBL
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TEXTS
init|=
literal|3
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|SDO_PROPERTY_COUNT
init|=
literal|4
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|EXTENDED_PROPERTY_COUNT
init|=
literal|0
decl_stmt|;
comment|/**      * The internal feature id for the '<em><b>Text</b></em>' attribute.<!--      * begin-user-doc --><!-- end-user-doc -->      *       * @generated      * @ordered      */
specifier|public
specifier|static
specifier|final
name|int
name|_INTERNAL_TEXT
init|=
literal|0
decl_stmt|;
comment|/**      * The internal feature id for the '<em><b>Int</b></em>' attribute.<!--      * begin-user-doc --><!-- end-user-doc -->      *       * @generated      * @ordered      */
specifier|public
specifier|static
specifier|final
name|int
name|_INTERNAL_INT
init|=
literal|1
decl_stmt|;
comment|/**      * The internal feature id for the '<em><b>Dbl</b></em>' attribute.<!--      * begin-user-doc --><!-- end-user-doc -->      *       * @generated      * @ordered      */
specifier|public
specifier|static
specifier|final
name|int
name|_INTERNAL_DBL
init|=
literal|2
decl_stmt|;
comment|/**      * The internal feature id for the '<em><b>Texts</b></em>' attribute list.      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      * @ordered      */
specifier|public
specifier|static
specifier|final
name|int
name|_INTERNAL_TEXTS
init|=
literal|3
decl_stmt|;
comment|/**      * The number of properties for this type.<!-- begin-user-doc --><!--      * end-user-doc -->      *       * @generated      * @ordered      */
specifier|public
specifier|static
specifier|final
name|int
name|INTERNAL_PROPERTY_COUNT
init|=
literal|4
decl_stmt|;
comment|/**      * The default value of the '{@link #getText()<em>Text</em>}' attribute.      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @see #getText()      * @generated      * @ordered      */
specifier|protected
specifier|static
specifier|final
name|String
name|TEXT_DEFAULT_
init|=
literal|null
decl_stmt|;
comment|/**      * The cached value of the '{@link #getText()<em>Text</em>}' attribute.      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @see #getText()      * @generated      * @ordered      */
specifier|protected
name|String
name|text
init|=
name|TEXT_DEFAULT_
decl_stmt|;
comment|/**      * This is true if the Text attribute has been set.<!-- begin-user-doc -->      *<!-- end-user-doc -->      *       * @generated      * @ordered      */
specifier|protected
name|boolean
name|text_set_
init|=
literal|false
decl_stmt|;
comment|/**      * The default value of the '{@link #getInt()<em>Int</em>}' attribute.<!--      * begin-user-doc --><!-- end-user-doc -->      *       * @see #getInt()      * @generated      * @ordered      */
specifier|protected
specifier|static
specifier|final
name|int
name|INT_DEFAULT_
init|=
literal|0
decl_stmt|;
comment|/**      * The cached value of the '{@link #getInt()<em>Int</em>}' attribute.<!--      * begin-user-doc --><!-- end-user-doc -->      *       * @see #getInt()      * @generated      * @ordered      */
specifier|protected
name|int
name|int_
init|=
name|INT_DEFAULT_
decl_stmt|;
comment|/**      * This is true if the Int attribute has been set.<!-- begin-user-doc -->      *<!-- end-user-doc -->      *       * @generated      * @ordered      */
specifier|protected
name|boolean
name|int_set_
init|=
literal|false
decl_stmt|;
comment|/**      * The default value of the '{@link #getDbl()<em>Dbl</em>}' attribute.<!--      * begin-user-doc --><!-- end-user-doc -->      *       * @see #getDbl()      * @generated      * @ordered      */
specifier|protected
specifier|static
specifier|final
name|double
name|DBL_DEFAULT_
init|=
literal|0.0
decl_stmt|;
comment|/**      * The cached value of the '{@link #getDbl()<em>Dbl</em>}' attribute.<!--      * begin-user-doc --><!-- end-user-doc -->      *       * @see #getDbl()      * @generated      * @ordered      */
specifier|protected
name|double
name|dbl
init|=
name|DBL_DEFAULT_
decl_stmt|;
comment|/**      * This is true if the Dbl attribute has been set.<!-- begin-user-doc -->      *<!-- end-user-doc -->      *       * @generated      * @ordered      */
specifier|protected
name|boolean
name|dbl_set_
init|=
literal|false
decl_stmt|;
comment|/**      * The cached value of the '{@link #getTexts()<em>Texts</em>}' attribute      * list.<!-- begin-user-doc --><!-- end-user-doc -->      *       * @see #getTexts()      * @generated      * @ordered      */
specifier|protected
name|List
name|texts
init|=
literal|null
decl_stmt|;
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|StructureImpl
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|int
name|internalConvertIndex
parameter_list|(
name|int
name|internalIndex
parameter_list|)
block|{
switch|switch
condition|(
name|internalIndex
condition|)
block|{
case|case
name|_INTERNAL_TEXT
case|:
return|return
name|TEXT
return|;
case|case
name|_INTERNAL_INT
case|:
return|return
name|INT
return|;
case|case
name|_INTERNAL_DBL
case|:
return|return
name|DBL
return|;
case|case
name|_INTERNAL_TEXTS
case|:
return|return
name|TEXTS
return|;
block|}
return|return
name|super
operator|.
name|internalConvertIndex
argument_list|(
name|internalIndex
argument_list|)
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|Type
name|getStaticType
parameter_list|()
block|{
return|return
operator|(
operator|(
name|SdoFactoryImpl
operator|)
name|SdoFactory
operator|.
name|INSTANCE
operator|)
operator|.
name|getStructure
argument_list|()
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|int
name|getStaticPropertyCount
parameter_list|()
block|{
return|return
name|INTERNAL_PROPERTY_COUNT
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|String
name|getText
parameter_list|()
block|{
return|return
name|text
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|void
name|setText
parameter_list|(
name|String
name|newText
parameter_list|)
block|{
name|String
name|oldText
init|=
name|text
decl_stmt|;
name|text
operator|=
name|newText
expr_stmt|;
name|boolean
name|oldText_set_
init|=
name|text_set_
decl_stmt|;
name|text_set_
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|isNotifying
argument_list|()
condition|)
block|{
name|notify
argument_list|(
name|ChangeKind
operator|.
name|SET
argument_list|,
name|_INTERNAL_TEXT
argument_list|,
name|oldText
argument_list|,
name|text
argument_list|,
operator|!
name|oldText_set_
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|void
name|unsetText
parameter_list|()
block|{
name|String
name|oldText
init|=
name|text
decl_stmt|;
name|boolean
name|oldText_set_
init|=
name|text_set_
decl_stmt|;
name|text
operator|=
name|TEXT_DEFAULT_
expr_stmt|;
name|text_set_
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|isNotifying
argument_list|()
condition|)
block|{
name|notify
argument_list|(
name|ChangeKind
operator|.
name|UNSET
argument_list|,
name|_INTERNAL_TEXT
argument_list|,
name|oldText
argument_list|,
name|TEXT_DEFAULT_
argument_list|,
name|oldText_set_
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|boolean
name|isSetText
parameter_list|()
block|{
return|return
name|text_set_
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|int
name|getInt
parameter_list|()
block|{
return|return
name|int_
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|void
name|setInt
parameter_list|(
name|int
name|newInt
parameter_list|)
block|{
name|int
name|oldInt
init|=
name|int_
decl_stmt|;
name|int_
operator|=
name|newInt
expr_stmt|;
name|boolean
name|oldInt_set_
init|=
name|int_set_
decl_stmt|;
name|int_set_
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|isNotifying
argument_list|()
condition|)
block|{
name|notify
argument_list|(
name|ChangeKind
operator|.
name|SET
argument_list|,
name|_INTERNAL_INT
argument_list|,
name|oldInt
argument_list|,
name|int_
argument_list|,
operator|!
name|oldInt_set_
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|void
name|unsetInt
parameter_list|()
block|{
name|int
name|oldInt
init|=
name|int_
decl_stmt|;
name|boolean
name|oldInt_set_
init|=
name|int_set_
decl_stmt|;
name|int_
operator|=
name|INT_DEFAULT_
expr_stmt|;
name|int_set_
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|isNotifying
argument_list|()
condition|)
block|{
name|notify
argument_list|(
name|ChangeKind
operator|.
name|UNSET
argument_list|,
name|_INTERNAL_INT
argument_list|,
name|oldInt
argument_list|,
name|INT_DEFAULT_
argument_list|,
name|oldInt_set_
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|boolean
name|isSetInt
parameter_list|()
block|{
return|return
name|int_set_
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|double
name|getDbl
parameter_list|()
block|{
return|return
name|dbl
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|void
name|setDbl
parameter_list|(
name|double
name|newDbl
parameter_list|)
block|{
name|double
name|oldDbl
init|=
name|dbl
decl_stmt|;
name|dbl
operator|=
name|newDbl
expr_stmt|;
name|boolean
name|oldDbl_set_
init|=
name|dbl_set_
decl_stmt|;
name|dbl_set_
operator|=
literal|true
expr_stmt|;
if|if
condition|(
name|isNotifying
argument_list|()
condition|)
block|{
name|notify
argument_list|(
name|ChangeKind
operator|.
name|SET
argument_list|,
name|_INTERNAL_DBL
argument_list|,
name|oldDbl
argument_list|,
name|dbl
argument_list|,
operator|!
name|oldDbl_set_
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|void
name|unsetDbl
parameter_list|()
block|{
name|double
name|oldDbl
init|=
name|dbl
decl_stmt|;
name|boolean
name|oldDbl_set_
init|=
name|dbl_set_
decl_stmt|;
name|dbl
operator|=
name|DBL_DEFAULT_
expr_stmt|;
name|dbl_set_
operator|=
literal|false
expr_stmt|;
if|if
condition|(
name|isNotifying
argument_list|()
condition|)
block|{
name|notify
argument_list|(
name|ChangeKind
operator|.
name|UNSET
argument_list|,
name|_INTERNAL_DBL
argument_list|,
name|oldDbl
argument_list|,
name|DBL_DEFAULT_
argument_list|,
name|oldDbl_set_
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|boolean
name|isSetDbl
parameter_list|()
block|{
return|return
name|dbl_set_
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|List
name|getTexts
parameter_list|()
block|{
if|if
condition|(
name|texts
operator|==
literal|null
condition|)
block|{
name|texts
operator|=
name|createPropertyList
argument_list|(
name|ListKind
operator|.
name|DATATYPE
argument_list|,
name|String
operator|.
name|class
argument_list|,
name|TEXTS
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
return|return
name|texts
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|Object
name|get
parameter_list|(
name|int
name|propertyIndex
parameter_list|,
name|boolean
name|resolve
parameter_list|)
block|{
switch|switch
condition|(
name|propertyIndex
condition|)
block|{
case|case
name|TEXT
case|:
return|return
name|getText
argument_list|()
return|;
case|case
name|INT
case|:
return|return
operator|new
name|Integer
argument_list|(
name|getInt
argument_list|()
argument_list|)
return|;
case|case
name|DBL
case|:
return|return
operator|new
name|Double
argument_list|(
name|getDbl
argument_list|()
argument_list|)
return|;
case|case
name|TEXTS
case|:
return|return
name|getTexts
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|get
argument_list|(
name|propertyIndex
argument_list|,
name|resolve
argument_list|)
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|set
parameter_list|(
name|int
name|propertyIndex
parameter_list|,
name|Object
name|newValue
parameter_list|)
block|{
switch|switch
condition|(
name|propertyIndex
condition|)
block|{
case|case
name|TEXT
case|:
name|setText
argument_list|(
operator|(
name|String
operator|)
name|newValue
argument_list|)
expr_stmt|;
return|return;
case|case
name|INT
case|:
name|setInt
argument_list|(
operator|(
operator|(
name|Integer
operator|)
name|newValue
operator|)
operator|.
name|intValue
argument_list|()
argument_list|)
expr_stmt|;
return|return;
case|case
name|DBL
case|:
name|setDbl
argument_list|(
operator|(
operator|(
name|Double
operator|)
name|newValue
operator|)
operator|.
name|doubleValue
argument_list|()
argument_list|)
expr_stmt|;
return|return;
case|case
name|TEXTS
case|:
name|getTexts
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|getTexts
argument_list|()
operator|.
name|addAll
argument_list|(
operator|(
name|Collection
operator|)
name|newValue
argument_list|)
expr_stmt|;
return|return;
block|}
name|super
operator|.
name|set
argument_list|(
name|propertyIndex
argument_list|,
name|newValue
argument_list|)
expr_stmt|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|void
name|unset
parameter_list|(
name|int
name|propertyIndex
parameter_list|)
block|{
switch|switch
condition|(
name|propertyIndex
condition|)
block|{
case|case
name|TEXT
case|:
name|unsetText
argument_list|()
expr_stmt|;
return|return;
case|case
name|INT
case|:
name|unsetInt
argument_list|()
expr_stmt|;
return|return;
case|case
name|DBL
case|:
name|unsetDbl
argument_list|()
expr_stmt|;
return|return;
case|case
name|TEXTS
case|:
name|getTexts
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return;
block|}
name|super
operator|.
name|unset
argument_list|(
name|propertyIndex
argument_list|)
expr_stmt|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|boolean
name|isSet
parameter_list|(
name|int
name|propertyIndex
parameter_list|)
block|{
switch|switch
condition|(
name|propertyIndex
condition|)
block|{
case|case
name|TEXT
case|:
return|return
name|isSetText
argument_list|()
return|;
case|case
name|INT
case|:
return|return
name|isSetInt
argument_list|()
return|;
case|case
name|DBL
case|:
return|return
name|isSetDbl
argument_list|()
return|;
case|case
name|TEXTS
case|:
return|return
name|texts
operator|!=
literal|null
operator|&&
operator|!
name|texts
operator|.
name|isEmpty
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|isSet
argument_list|(
name|propertyIndex
argument_list|)
return|;
block|}
comment|/**      *<!-- begin-user-doc --><!-- end-user-doc -->      *       * @generated      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|isProxy
argument_list|(
name|this
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
return|;
block|}
name|StringBuffer
name|result
init|=
operator|new
name|StringBuffer
argument_list|(
name|super
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|result
operator|.
name|append
argument_list|(
literal|" (text: "
argument_list|)
expr_stmt|;
if|if
condition|(
name|text_set_
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|append
argument_list|(
literal|"<unset>"
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|append
argument_list|(
literal|", int: "
argument_list|)
expr_stmt|;
if|if
condition|(
name|int_set_
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|int_
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|append
argument_list|(
literal|"<unset>"
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|append
argument_list|(
literal|", dbl: "
argument_list|)
expr_stmt|;
if|if
condition|(
name|dbl_set_
condition|)
block|{
name|result
operator|.
name|append
argument_list|(
name|dbl
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|.
name|append
argument_list|(
literal|"<unset>"
argument_list|)
expr_stmt|;
block|}
name|result
operator|.
name|append
argument_list|(
literal|", texts: "
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
name|texts
argument_list|)
expr_stmt|;
name|result
operator|.
name|append
argument_list|(
literal|')'
argument_list|)
expr_stmt|;
return|return
name|result
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

begin_comment
comment|//CHECKSTYLE:ON
end_comment

end_unit

