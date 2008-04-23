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
name|aegis
operator|.
name|type
operator|.
name|missing
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * A Outer, for purposes of matching and indexing.  */
end_comment

begin_class
specifier|public
class|class
name|Outer
implements|implements
name|Serializable
block|{
comment|// Note that the accessors in here don't return null pointers for strings.
comment|// This improves the behavior of web services that return examples of this
comment|// object.
comment|/**      *       */
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|2435297692897827392L
decl_stmt|;
comment|/**      * The name string itself.      */
name|String
name|data
decl_stmt|;
comment|/**      * A unique ID for the name. Often a key from some other database.      */
name|String
name|uid
decl_stmt|;
comment|/**      * A flag indicating that this name is the primary name of it's entity.      */
name|boolean
name|primary
decl_stmt|;
comment|/**      * The type of name in the taxonomy of entity types.      *       * @see com.basistech.rlp.RLPNEConstants for constants for this field.      */
name|int
name|entityType
decl_stmt|;
comment|/**      * The unique identifier of the entity containing this name, or none.      *       * @see Entity      */
name|String
name|entityUID
decl_stmt|;
comment|/**      * In some environments, names store additional data.      */
name|String
name|extra
decl_stmt|;
comment|/**      * Any pre-computed inners for the name.      *       * @@uml.property name="inners"      * @@uml.associationEnd multiplicity="(0 -1)"      */
name|Inner
index|[]
name|inners
decl_stmt|;
comment|/**      * Construct an empty name object.      */
specifier|public
name|Outer
parameter_list|()
block|{
name|extra
operator|=
literal|""
expr_stmt|;
block|}
comment|/**      * @return arbitrary data stored with this name.      */
specifier|public
name|String
name|getExtra
parameter_list|()
block|{
return|return
name|extra
operator|==
literal|null
condition|?
literal|""
else|:
name|extra
return|;
block|}
comment|/**      * Set arbitrary data stored with this name.      *       * @param extra the extra to set      */
specifier|public
name|void
name|setExtra
parameter_list|(
name|String
name|extra
parameter_list|)
block|{
name|this
operator|.
name|extra
operator|=
name|extra
expr_stmt|;
block|}
comment|/**      * Set a unique ID for this name. This API does not check or enforce      * uniqueness.      *       * @param uid      */
specifier|public
name|void
name|setUID
parameter_list|(
name|String
name|auid
parameter_list|)
block|{
name|this
operator|.
name|uid
operator|=
name|auid
expr_stmt|;
block|}
comment|/**      * @return the unique ID for this name.      */
specifier|public
name|String
name|getUID
parameter_list|()
block|{
return|return
name|uid
return|;
block|}
comment|/**      * Set the textual content of the name. This call does not automatically set      * any other properties, such as script or language.      *       * @param data the data to set.      */
specifier|public
name|void
name|setData
parameter_list|(
name|String
name|data
parameter_list|)
block|{
name|this
operator|.
name|data
operator|=
name|data
expr_stmt|;
block|}
comment|/**      * @return the textual content of the name.      */
specifier|public
name|String
name|getData
parameter_list|()
block|{
return|return
name|data
return|;
block|}
comment|/**      * Set the 'named entity' type of this name.      * {@link com.basistech.rlp.RLPNEConstants} for possible values. This value      * influences the interpretating and matching of the name. Use the value      * {@link com.basistech.rlp.RLPNEConstants#NE_TYPE_NONE} if there is no type      * available.      *       * @param entityType      */
specifier|public
name|void
name|setEntityType
parameter_list|(
name|int
name|entityType
parameter_list|)
block|{
name|this
operator|.
name|entityType
operator|=
name|entityType
expr_stmt|;
block|}
comment|/**      * @return the 'named entity' type of this name.      */
specifier|public
name|int
name|getEntityType
parameter_list|()
block|{
return|return
name|entityType
return|;
block|}
comment|/**      * Set an entity UID for this name. Entities group multiple names for a      * single real-world item. All the names of a single entity are connected      * via their entity unique ID.      *       * @param entityUID the UID.      */
specifier|public
name|void
name|setEntityUID
parameter_list|(
name|String
name|entityUID
parameter_list|)
block|{
name|this
operator|.
name|entityUID
operator|=
name|entityUID
expr_stmt|;
block|}
comment|/**      * @return the entity unique ID.      */
specifier|public
name|String
name|getEntityUID
parameter_list|()
block|{
return|return
name|entityUID
return|;
block|}
comment|/**      * Set the 'primary' flag for this name. If names are grouped by entities      * {@link #setEntityUID(String)}, one of the names of an entity may be      * marked primary. This API does not check that only one name is marked.      *       * @param primary the primary flag.      */
specifier|public
name|void
name|setPrimary
parameter_list|(
name|boolean
name|primary
parameter_list|)
block|{
name|this
operator|.
name|primary
operator|=
name|primary
expr_stmt|;
block|}
comment|/**      * @return the primary flag.      */
specifier|public
name|boolean
name|isPrimary
parameter_list|()
block|{
return|return
name|primary
return|;
block|}
comment|/**      * Set pre-calculated inners for this name.      *       * @param inners the inners.      */
specifier|public
name|void
name|setTransliterations
parameter_list|(
name|Inner
index|[]
name|transliterations
parameter_list|)
block|{
name|this
operator|.
name|inners
operator|=
name|transliterations
expr_stmt|;
block|}
comment|/**      * @return pre-calculated inners for this name.      */
specifier|public
name|Inner
index|[]
name|getTransliterations
parameter_list|()
block|{
return|return
name|inners
return|;
block|}
block|}
end_class

end_unit

